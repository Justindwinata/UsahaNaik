# Final Screenshot Capture Guide

Runtime screenshots are not committed unless they are captured from a running Android emulator/device.

## Current Status

During this task, `adb devices` returned no attached emulator/device. Therefore, no runtime screenshots were captured and no runtime screenshot claim is made.

## Recommended Screenshot Names

Capture these screens from a Medium Phone emulator:

- `01_dashboard.png`
- `02_finance_form.png`
- `03_weekly_plan.png`
- `04_content_planner.png`
- `05_report.png`
- `06_profile_reminders.png`

## Capture Command

When an emulator/device is attached, run:

```bash
python3 scripts/capture_project_screenshots.py
```

The script captures the current visible screen only. Navigate manually through the app and rename each capture according to the checklist.

## Manual QA Notes

- Use Demo Mode before capture if you need populated Dashboard, Plan, Ideas, Report, and Profile screens.
- Do not use mockups as runtime screenshots.
- Keep screenshots under `docs/evidence/screenshots/final/`.
