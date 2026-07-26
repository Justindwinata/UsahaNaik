#!/usr/bin/env python3
"""Build a clean source-only ZIP archive for the current project."""

from __future__ import annotations

import hashlib
import os
import shutil
import subprocess
import sys
import zipfile
from datetime import datetime, timezone
from pathlib import Path


PROJECT_ROOT = Path.cwd().resolve()
PROJECT_NAME = PROJECT_ROOT.name
DIST_DIR = PROJECT_ROOT / "dist"
STAGING_DIR = DIST_DIR / f".{PROJECT_NAME}_zip_staging"
ZIP_NAME = f"{PROJECT_NAME}_Project_Source.zip"
ZIP_PATH = DIST_DIR / ZIP_NAME

FORBIDDEN_DIRS = {
    ".git",
    ".gradle",
    ".idea",
    ".kotlin",
    ".mypy_cache",
    ".pytest_cache",
    ".ruff_cache",
    ".venv",
    ".vscode",
    "__pycache__",
    ".ipynb_checkpoints",
    "build",
    "captures",
    "checkpoints",
    "coverage",
    "dist",
    "env",
    "ENV",
    "htmlcov",
    "models",
    "node_modules",
    "out",
    "outputs",
    "target",
    "venv",
}

FORBIDDEN_FILE_NAMES = {
    ".DS_Store",
    "Thumbs.db",
    "local.properties",
    ".env",
    ".env.local",
    ".env.production",
    ".env.development",
    "adapter_model.safetensors",
}

FORBIDDEN_SUFFIXES = {
    ".7z",
    ".aab",
    ".apk",
    ".bak",
    ".bin",
    ".ckpt",
    ".class",
    ".gguf",
    ".h5",
    ".iml",
    ".keras",
    ".log",
    ".onnx",
    ".pt",
    ".pth",
    ".pyc",
    ".pyd",
    ".pyo",
    ".rar",
    ".safetensors",
    ".swp",
    ".tar",
    ".temp",
    ".tmp",
    ".war",
    ".zip",
}

FORBIDDEN_DOUBLE_SUFFIXES = {
    ".tar.gz",
}

GRADLE_WRAPPER_JAR = Path("gradle/wrapper/gradle-wrapper.jar")


def run_git(args: list[str], default: str = "unknown") -> str:
    try:
        return subprocess.check_output(["git", *args], cwd=PROJECT_ROOT, text=True).strip() or default
    except Exception:
        return default


def is_forbidden(path: Path) -> tuple[bool, str]:
    rel = path.relative_to(PROJECT_ROOT)
    rel_posix = rel.as_posix()
    parts = set(rel.parts)

    if parts & FORBIDDEN_DIRS:
        return True, f"directory:{sorted(parts & FORBIDDEN_DIRS)[0]}"

    if path.name in FORBIDDEN_FILE_NAMES:
        return True, f"file:{path.name}"

    if any(rel_posix.endswith(suffix) for suffix in FORBIDDEN_DOUBLE_SUFFIXES):
        return True, "archive:tar.gz"

    suffix = path.suffix.lower()
    if suffix == ".jar" and rel == GRADLE_WRAPPER_JAR:
        return False, ""
    if suffix in FORBIDDEN_SUFFIXES or suffix == ".jar":
        return True, f"suffix:{suffix}"

    if rel_posix.startswith("reports/tmp/"):
        return True, "path:reports/tmp"

    if rel_posix.startswith(".github/workflows/") and suffix == ".log":
        return True, "path:.github/workflows/*.log"

    return False, ""


def project_kind() -> str:
    if (PROJECT_ROOT / "settings.gradle.kts").exists() or (PROJECT_ROOT / "build.gradle.kts").exists():
        return "Android/Gradle"
    if (PROJECT_ROOT / "package.json").exists():
        return "Node"
    if (PROJECT_ROOT / "requirements.txt").exists() or (PROJECT_ROOT / "pyproject.toml").exists():
        return "Python"
    return "Generic source"


def run_commands() -> list[str]:
    commands: list[str] = []
    if project_kind() == "Android/Gradle":
        commands.extend(
            [
                "ANDROID_HOME=/Users/justindwinata/Library/Android/sdk ./gradlew test",
                "ANDROID_HOME=/Users/justindwinata/Library/Android/sdk ./gradlew assembleDebug",
            ]
        )
    elif project_kind() == "Python":
        commands.append("python3 -m compileall .")
        if (PROJECT_ROOT / "tests").exists():
            commands.append("pytest -q")
    elif project_kind() == "Node":
        commands.append("npm install")
        commands.append("npm test / npm run build if defined in package.json")
    return commands


def manifest_text(included_files: list[Path], excluded_summary: dict[str, int]) -> str:
    top_level = sorted({path.parts[0] for path in included_files if path.parts})
    generated_at = datetime.now(timezone.utc).strftime("%Y-%m-%d %H:%M:%S UTC")
    commands = run_commands()
    command_text = "\n".join(f"- `{command}`" for command in commands) or "- Inspect project docs for run/test commands."
    excluded_text = "\n".join(
        f"- `{reason}`: {count}" for reason, count in sorted(excluded_summary.items())
    ) or "- No excluded files were encountered."

    return f"""# Project ZIP Manifest

## Archive

- Project name: {PROJECT_NAME}
- Generated timestamp: {generated_at}
- Git commit hash: {run_git(["rev-parse", "HEAD"])}
- Git branch: {run_git(["branch", "--show-current"])}
- Repository remote URL: {run_git(["remote", "get-url", "origin"])}
- ZIP file name: {ZIP_NAME}
- Project kind: {project_kind()}

## Included Top-Level Folders/Files

{chr(10).join(f"- `{item}`" for item in top_level)}

## Excluded Folders/Files Summary

{excluded_text}

## How To Run Project

{command_text}

## How To Test Project

{command_text}

## Known Local Setup Requirements

- Android SDK is required for this project.
- Use Android Studio or the included Gradle wrapper.
- Generated build outputs, local SDK paths, caches, Git history, previous ZIP files, and local secrets are intentionally excluded.
"""


def copy_project_files() -> tuple[list[Path], dict[str, int]]:
    if STAGING_DIR.exists():
        shutil.rmtree(STAGING_DIR)
    STAGING_DIR.mkdir(parents=True)

    included: list[Path] = []
    excluded_summary: dict[str, int] = {}

    for source in sorted(PROJECT_ROOT.rglob("*")):
        if source == STAGING_DIR or STAGING_DIR in source.parents:
            continue
        forbidden, reason = is_forbidden(source)
        if forbidden:
            excluded_summary[reason] = excluded_summary.get(reason, 0) + 1
            if source.is_dir():
                continue
            continue
        if source.is_dir():
            continue

        rel = source.relative_to(PROJECT_ROOT)
        target = STAGING_DIR / rel
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(source, target)
        included.append(rel)

    manifest = manifest_text(included, excluded_summary)
    (STAGING_DIR / "PROJECT_ZIP_MANIFEST.md").write_text(manifest, encoding="utf-8")
    included.append(Path("PROJECT_ZIP_MANIFEST.md"))
    return included, excluded_summary


def create_zip() -> None:
    if ZIP_PATH.exists():
        ZIP_PATH.unlink()
    with zipfile.ZipFile(ZIP_PATH, mode="w", compression=zipfile.ZIP_DEFLATED) as archive:
        for file_path in sorted(STAGING_DIR.rglob("*")):
            if file_path.is_file():
                archive.write(file_path, file_path.relative_to(STAGING_DIR).as_posix())


def sha256(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as handle:
        for chunk in iter(lambda: handle.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()


def human_size(path: Path) -> str:
    size = path.stat().st_size
    for unit in ["B", "KB", "MB", "GB"]:
        if size < 1024:
            return f"{size:.1f} {unit}"
        size /= 1024
    return f"{size:.1f} TB"


def main() -> int:
    DIST_DIR.mkdir(exist_ok=True)
    try:
        included, excluded_summary = copy_project_files()
        create_zip()
    finally:
        if STAGING_DIR.exists():
            shutil.rmtree(STAGING_DIR)

    print(f"Project name: {PROJECT_NAME}")
    print(f"Output ZIP path: {ZIP_PATH}")
    print(f"ZIP size: {human_size(ZIP_PATH)}")
    print(f"Included file count: {len(included)}")
    print("Excluded pattern summary:")
    for reason, count in sorted(excluded_summary.items()):
        print(f"  - {reason}: {count}")
    print(f"SHA256: {sha256(ZIP_PATH)}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
