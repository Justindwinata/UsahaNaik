# QA Checklist

## Automated Validation

Run:

```bash
ANDROID_HOME=/Users/justindwinata/Library/Android/sdk ./gradlew test
ANDROID_HOME=/Users/justindwinata/Library/Android/sdk ./gradlew assembleDebug
git diff --check
```

Expected:

- Unit tests pass.
- Debug APK assembles.
- No whitespace errors.

## Manual QA Checklist

Use an emulator/device when available.

- App launches without crash.
- Welcome screen opens.
- Category selection works.
- Business setup form validates required fields.
- Setup review saves profile.
- Profile can delete local profile.
- Profile can load demo data after confirmation.
- Profile can clear demo data after confirmation.
- Dashboard reloads and shows demo data.
- Financial entries appear and can be deleted.
- Weekly Plan shows demo tasks and milestones.
- Task completion toggles persist.
- Content Planner shows saved demo ideas.
- Content ideas can be generated locally.
- Saved idea can be favorited, planned, done, and deleted.
- Saved idea can be scheduled locally.
- Calendar item status can change to planned, posted, skipped, or done.
- Retrospective screen loads demo snapshot/history.
- Retrospective can generate and save a new review.
- Business Report opens.
- Report period selector works.
- Export-ready report text appears.
- Report snapshot can be saved.
- Bottom navigation routes are reachable.
- No dead-end screen.
- Empty states provide clear next action.

## Accessibility Pass

- Important bottom navigation icons have content descriptions.
- Status/severity is visible as text labels, not color only.
- Buttons use readable labels.
- Cards and text remain readable on small screens with vertical scroll.
- Touch targets are visually large enough for common actions.

## Known Manual QA Limitation

If no emulator/device is attached, runtime QA cannot be honestly claimed. In that case, report automated validation only.
