#!/usr/bin/env python3
"""Verify the clean source-only project ZIP archive."""

from __future__ import annotations

import sys
import zipfile
from pathlib import Path


PROJECT_ROOT = Path.cwd().resolve()
PROJECT_NAME = PROJECT_ROOT.name
ZIP_PATH = PROJECT_ROOT / "dist" / f"{PROJECT_NAME}_Project_Source.zip"

FORBIDDEN_DIR_PREFIXES = (
    ".git/",
    ".gradle/",
    ".venv/",
    "__pycache__/",
    "build/",
    "node_modules/",
)

FORBIDDEN_DIR_PARTS = {
    ".git",
    ".gradle",
    ".venv",
    "__pycache__",
    "build",
    "node_modules",
}

ARCHIVE_SUFFIXES = (".zip", ".tar", ".tar.gz", ".7z", ".rar")
MODEL_SUFFIXES = (".safetensors", ".bin", ".pt", ".pth", ".onnx", ".gguf", ".ckpt", ".h5", ".keras")
MODEL_NAMES = {"adapter_model.safetensors"}


def has_path(entries: set[str], path: str) -> bool:
    normalized = path.rstrip("/")
    return normalized in entries or any(entry.startswith(f"{normalized}/") for entry in entries)


def forbidden_dir_entries(entries: list[str]) -> list[str]:
    offenders: list[str] = []
    for entry in entries:
        if entry.startswith(FORBIDDEN_DIR_PREFIXES):
            offenders.append(entry)
            continue
        parts = set(Path(entry).parts)
        if parts & FORBIDDEN_DIR_PARTS:
            offenders.append(entry)
    return offenders


def verify() -> tuple[bool, list[str]]:
    errors: list[str] = []
    if not ZIP_PATH.exists():
        return False, [f"ZIP not found: {ZIP_PATH}"]

    with zipfile.ZipFile(ZIP_PATH) as archive:
        entries = archive.namelist()
        entry_set = set(entries)

    if "PROJECT_ZIP_MANIFEST.md" not in entry_set:
        errors.append("PROJECT_ZIP_MANIFEST.md is missing.")

    forbidden_dirs = forbidden_dir_entries(entries)
    if forbidden_dirs:
        errors.append(f"Forbidden directory entries found: {forbidden_dirs[:10]}")

    previous_archives = [
        entry for entry in entries
        if entry != ZIP_PATH.name and entry.lower().endswith(ARCHIVE_SUFFIXES)
    ]
    if previous_archives:
        errors.append(f"Archive files found inside ZIP: {previous_archives[:10]}")

    model_artifacts = [
        entry for entry in entries
        if Path(entry).name in MODEL_NAMES or entry.lower().endswith(MODEL_SUFFIXES)
    ]
    if model_artifacts:
        errors.append(f"Large model/checkpoint artifacts found: {model_artifacts[:10]}")

    if (PROJECT_ROOT / "README.md").exists() and "README.md" not in entry_set:
        errors.append("README.md exists in project but is missing from ZIP.")

    if (PROJECT_ROOT / "docs").exists() and not has_path(entry_set, "docs"):
        errors.append("docs/ exists in project but is missing from ZIP.")

    source_roots = ["app", "src", "backend", "frontend"]
    present_source_roots = [root for root in source_roots if (PROJECT_ROOT / root).exists()]
    if present_source_roots and not any(has_path(entry_set, root) for root in present_source_roots):
        errors.append(f"Project source root missing from ZIP. Expected one of: {present_source_roots}")

    return not errors, errors


def main() -> int:
    passed, errors = verify()
    print(f"Project name: {PROJECT_NAME}")
    print(f"ZIP path: {ZIP_PATH}")
    if passed:
        print("Verification: PASS")
        print("Checked: forbidden folders, nested archives, model artifacts, manifest, docs, and source roots.")
        return 0

    print("Verification: FAIL")
    for error in errors:
        print(f"- {error}")
    return 1


if __name__ == "__main__":
    sys.exit(main())
