#!/usr/bin/env python3
"""Verify the clean visual-only package for portfolio and LinkedIn assets."""

from __future__ import annotations

import sys
import zipfile
from pathlib import Path


REQUIRED_FILES = {
    "VISUAL_PACKAGE_README.md",
    "VISUAL_PACKAGE_MANIFEST.md",
    "mockups/mockup_desktop.png",
    "mockups/mockup_mobile.png",
    "mockups/mockup_showcase.png",
    "linkedin_campaign/linkedin_cover.png",
    "linkedin_campaign/linkedin_square_post.png",
    "linkedin_campaign/linkedin_carousel_01_intro.png",
    "linkedin_campaign/linkedin_carousel_02_features.png",
    "linkedin_campaign/linkedin_carousel_03_tech_stack.png",
    "linkedin_campaign/linkedin_carousel_04_demo.png",
    "linkedin_campaign/linkedin_carousel_05_closing.png",
    "linkedin_campaign/linkedin_banner.png",
}

FORBIDDEN_PARTS = {
    ".git",
    ".gradle",
    ".idea",
    ".venv",
    "__pycache__",
    "build",
    "node_modules",
    "venv",
}

FORBIDDEN_SUFFIXES = {
    ".7z",
    ".aab",
    ".apk",
    ".bin",
    ".ckpt",
    ".gguf",
    ".h5",
    ".jar",
    ".keras",
    ".onnx",
    ".pt",
    ".pth",
    ".rar",
    ".safetensors",
    ".tar",
    ".zip",
}


def main() -> int:
    project_dir = Path.cwd()
    project_name = project_dir.name
    zip_path = project_dir / "dist" / f"{project_name}_LinkedIn_Visual_Package.zip"

    failures: list[str] = []
    if not zip_path.exists():
        print(f"FAIL: ZIP not found: {zip_path}")
        return 1

    with zipfile.ZipFile(zip_path) as archive:
        names = set(archive.namelist())

    for required in sorted(REQUIRED_FILES):
        if required not in names:
            failures.append(f"Missing required file: {required}")

    required_dirs = {"screenshots/", "mockups/", "linkedin_campaign/"}
    for required_dir in required_dirs:
        if not any(name.startswith(required_dir) for name in names):
            failures.append(f"Missing required folder content: {required_dir}")

    for name in sorted(names):
        path = Path(name)
        if any(part in FORBIDDEN_PARTS for part in path.parts):
            failures.append(f"Forbidden folder included: {name}")
        if path.suffix.lower() in FORBIDDEN_SUFFIXES:
            failures.append(f"Forbidden archive/artifact included: {name}")

    if failures:
        print("FAIL: Visual package verification failed.")
        for failure in failures:
            print(f"- {failure}")
        return 1

    print("PASS: Visual package verification passed.")
    print(f"ZIP path: {zip_path}")
    print(f"File count: {len(names)}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
