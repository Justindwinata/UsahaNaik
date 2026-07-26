# Project ZIP Packaging Guide

## Purpose

The project ZIP archive is a clean source-only backup/submission package for UsahaNaik. It is intended for portfolio review, project transfer, and future development handoff.

The ZIP is not a Git replacement and does not include local build output, installed dependencies, cache folders, secrets, or Git history.

## What Is Included

The archive includes useful project files such as:

- Android source under `app/`
- Gradle project configuration
- Gradle wrapper files required to run the project
- `README.md`
- `CHANGELOG.md`
- `docs/`
- `scripts/`
- Project assets such as `UI_Reference.jpg`
- `PROJECT_ZIP_MANIFEST.md` generated inside the ZIP

## What Is Excluded

The archive excludes local or generated files such as:

- `.git/`
- `.gradle/`
- `.idea/`
- `.kotlin/`
- `build/`
- `app/build/`
- `dist/`
- previous ZIP files
- local SDK files such as `local.properties`
- `.env` files
- cache folders
- compiled outputs such as APK/AAB/class files
- large model/checkpoint artifacts

The Gradle wrapper jar is intentionally retained because it is part of the committed Android wrapper required to run `./gradlew`.

## Generate The ZIP

Run from the project root:

```bash
python3 scripts/build_project_zip.py
```

Output:

```text
dist/UsahaNaik_Project_Source.zip
```

The script prints:

- project name
- ZIP path
- ZIP size
- included file count
- excluded pattern summary
- SHA256 hash

## Verify The ZIP

Run:

```bash
python3 scripts/verify_project_zip.py
```

The verifier opens the ZIP and checks that:

- forbidden folders are not present
- nested ZIP/archive files are not present
- large model/checkpoint files are not present
- `PROJECT_ZIP_MANIFEST.md` exists
- important project anchors such as `README.md`, `docs/`, and `app/` are included

## Validation Before Packaging

For this Android project, run:

```bash
ANDROID_HOME=/Users/justindwinata/Library/Android/sdk ./gradlew test
ANDROID_HOME=/Users/justindwinata/Library/Android/sdk ./gradlew assembleDebug
git diff --check
git status --short --branch
```

Then generate and verify the ZIP:

```bash
python3 scripts/build_project_zip.py
python3 scripts/verify_project_zip.py
```

## Why These Exclusions Matter

- `.git/` is excluded because Git history is not needed inside a submission ZIP.
- Installed dependencies and caches are excluded because they are local machine artifacts.
- Build outputs are excluded because they can be regenerated.
- Secrets and `.env` files are excluded to prevent accidental credential leaks.
- Large model/checkpoint files are excluded because they are not source code and can make archives impractical.

## Regenerate Later

Delete any old local archive if needed, then run:

```bash
python3 scripts/build_project_zip.py
python3 scripts/verify_project_zip.py
```

Do not commit `dist/UsahaNaik_Project_Source.zip` unless explicitly requested.
