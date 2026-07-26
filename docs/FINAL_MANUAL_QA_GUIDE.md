# Final Manual QA Guide

UN-0015 final QA focuses on portfolio stability, local workflow correctness, and honest product positioning.

## Required Validation Commands

Run from the repository root:

```bash
ANDROID_HOME=/Users/justindwinata/Library/Android/sdk ./gradlew test
ANDROID_HOME=/Users/justindwinata/Library/Android/sdk ./gradlew assembleDebug
git diff --check
git status --short --branch
```

Expected result:

- Unit tests pass.
- Debug APK builds successfully.
- `git diff --check` returns no whitespace errors.
- Working tree is clean after the final commit.

## Emulator QA Flow

Use a Medium Phone emulator when available.

1. Fresh install the debug APK.
2. Launch the app.
3. Select Indonesian, then switch to English from Profile and back if needed.
4. Continue in local mode from the auth-ready entry.
5. Create a business profile or load demo data.
6. Confirm Dashboard content is not clipped by the status bar.
7. Confirm bottom navigation does not cover the last card.
8. Add one income entry and one expense entry.
9. Confirm Dashboard and Report metrics reflect local entries.
10. Generate or open Weekly Plan.
11. Complete one task and confirm task progress changes.
12. Generate and save Weekly Retrospective.
13. Generate content ideas, save one, mark it planned, and schedule it.
14. Confirm Saved Ideas and Content Calendar update.
15. Open Business Report and switch report periods.
16. Create a local reminder with a valid time.
17. Try invalid reminder time such as `99:99` and confirm validation appears.
18. Load demo data, inspect Dashboard, Ideas, Plan, Report, and Profile.
19. Clear demo data and confirm the app returns to an empty/local state.
20. Restart the app and confirm selected language and local data persistence.

## Honest QA Notes

- Notification delivery must be tested on an emulator/device before claiming runtime delivery.
- Screenshots should only be saved when actually captured from a running emulator/device.
- The app remains local-first and does not use backend auth, cloud sync, or a paid AI API.
