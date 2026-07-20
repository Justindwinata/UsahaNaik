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
- Welcome language selector switches Indonesian/English static labels.
- Login screen opens from Welcome.
- Register screen opens from Welcome/Login.
- Continue local mode works without real authentication.
- Password fields are not persisted.
- Category selection works.
- Business setup form validates required fields.
- Setup review saves profile.
- Profile can delete local profile.
- Profile can load demo data after confirmation.
- Profile can clear demo data after confirmation.
- Profile language switch updates visible static labels.
- Profile shows auth-ready local account placeholder.
- Profile shows local reminder permission status.
- Profile shows notification permission explanation and `Enable Notifications` action.
- Reminder form can create a daily finance reminder.
- Reminder can be enabled, paused, edited, and deleted.
- Reminder list remains usable if notification permission is denied.
- Reminder cards explain whether system notification scheduling is available or in-app fallback is active.
- Dashboard reloads and shows demo data.
- Dashboard shows active reminder count and next reminder.
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

## Bilingual/Auth QA Notes

- Default language should be Indonesian on first launch.
- English selection should persist after app restart.
- Authentication is not enabled; do not claim login/register creates a real account.
- Continue local mode should route to Dashboard if a saved profile exists and to setup if no profile exists.

## Accessibility Pass

- Important bottom navigation icons have content descriptions.
- Status/severity is visible as text labels, not color only.
- Reminder status is visible as text, not color only.
- Buttons use readable labels.
- Cards and text remain readable on small screens with vertical scroll.
- Touch targets are visually large enough for common actions.

## Known Manual QA Limitation

If no emulator/device is attached, runtime QA and notification behavior QA cannot be honestly claimed. In that case, report automated validation only.

## Notification QA Notes

UN-0012 schedules approximate reminder notifications through WorkManager when notification permission is available. Device/emulator QA should cover:

- Android 13+ permission prompt from user action.
- Permission granted behavior.
- Permission denied behavior.
- In-app fallback behavior.
- Cancelling scheduled notifications when reminders are paused or deleted.
- Notification tap opens the app.
- Demo reminders do not duplicate after loading demo data repeatedly.

Do not claim notification delivery was manually verified unless an emulator or physical Android device was used.
