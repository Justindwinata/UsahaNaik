# UN-0014 Manual Emulator QA Checklist

Use this checklist on a Medium Phone emulator or physical Android device after installing the debug build.

Do not mark an item complete unless it was actually tested on device/emulator.

## Environment

- [ ] Device/emulator attached in `adb devices`.
- [ ] Fresh install completed.
- [ ] App launches without crash.
- [ ] Screenshots saved under `docs/evidence/screenshots/un0014/` if captured.

## First Launch And Entry

- [ ] Welcome screen top content is not clipped by the status bar.
- [ ] Indonesian is the default language.
- [ ] Language can switch to English.
- [ ] Language switch persists after app restart.
- [ ] Login screen opens.
- [ ] Register screen opens.
- [ ] Continue local mode works without real authentication.
- [ ] Auth copy clearly says authentication is not enabled yet.

## Setup And Dashboard

- [ ] No-profile state guides user to business setup.
- [ ] Category selection routes to setup form.
- [ ] Business setup saves a local profile.
- [ ] After setup save, Dashboard opens with saved profile data.
- [ ] Dashboard top content is not clipped.
- [ ] Bottom navigation does not cover scroll content.
- [ ] Dashboard KPI cards are readable on medium phone.
- [ ] Business health and diagnosis are easy to scan.
- [ ] Quick actions route to Plan, Ideas, and Report.

## Financial Tracking

- [ ] Income entry with valid title, amount, category, and date saves locally.
- [ ] Expense entry saves locally.
- [ ] Invalid amount characters show validation feedback.
- [ ] Invalid date format shows validation feedback.
- [ ] Success feedback appears after saving.
- [ ] Recent financial activity updates after save.
- [ ] Dashboard metrics update after income/expense save.
- [ ] Delete financial entry works and updates metrics.

## Weekly Plan

- [ ] Weekly Plan tab opens.
- [ ] Empty state guides user to generate a plan.
- [ ] Generate Weekly Plan creates tasks, challenge, and milestones.
- [ ] Task card status is visible without relying only on color.
- [ ] Checking a task updates completed task count.
- [ ] Unchecking a task updates progress back.
- [ ] Milestone progress updates where related tasks exist.
- [ ] Regenerate plan confirmation appears before replacement.

## Retrospective

- [ ] Retrospective opens from Weekly Plan.
- [ ] Generate and save retrospective works.
- [ ] Progress snapshot appears after generation.
- [ ] Latest saved snapshot appears after returning to the screen.
- [ ] History list shows saved retrospectives.
- [ ] Copy states this is a planning summary, not guaranteed outcome.

## Content Planner And Calendar

- [ ] Ideas tab opens.
- [ ] Generate ideas creates local deterministic ideas.
- [ ] Generated idea card includes hook, angle, caption, CTA, visual, and safety note.
- [ ] Save Idea stores the idea locally.
- [ ] Saved generated idea changes to a saved state.
- [ ] Saved Ideas list updates.
- [ ] Favorite, planned, done, draft, and delete actions update saved idea state.
- [ ] Status filters work.
- [ ] Schedule action opens schedule form.
- [ ] Valid schedule saves to local content calendar.
- [ ] Calendar status can change to planned, posted, skipped, or done.
- [ ] Calendar item delete works.

## Business Report

- [ ] Report tab opens.
- [ ] Period selector changes report period.
- [ ] KPI cards render correctly.
- [ ] Simple charts/bars are readable.
- [ ] Report uses local finance, weekly, content, and retrospective data.
- [ ] Export-ready text appears.
- [ ] Save snapshot works.
- [ ] Disclaimer says report is not an official accounting/tax document.

## Profile, Demo, Reminders

- [ ] Profile tab opens.
- [ ] Language switch works from Profile.
- [ ] Local profile summary appears when profile exists.
- [ ] Demo data load confirmation appears.
- [ ] Load Demo Data seeds realistic sample data.
- [ ] Dashboard, Plan, Ideas, Report, and Profile become populated after demo load.
- [ ] Clear Demo Data confirmation appears.
- [ ] Clear Demo Data resets sample data safely.
- [ ] Reminder form validates title.
- [ ] Reminder form validates `HH:mm` time.
- [ ] One-time reminder validates date.
- [ ] Saving reminder adds it to saved reminder list.
- [ ] Pause, enable, edit, and delete reminder actions work.
- [ ] Notification permission state is clear.
- [ ] If notification permission is denied/unavailable, in-app reminder fallback remains visible.

## Restart Persistence

- [ ] Saved language persists after restart.
- [ ] Business profile persists after restart.
- [ ] Finance entries persist after restart.
- [ ] Weekly plan task status persists after restart.
- [ ] Saved content ideas and schedules persist after restart.
- [ ] Retrospective history persists after restart.
- [ ] Report snapshots persist after restart.
- [ ] Reminders persist after restart.

## Known Manual QA Limits

- [ ] If no emulator/device is attached, runtime QA remains user-side.
- [ ] Notification delivery must be verified on emulator/device before claiming delivery behavior.
- [ ] Screenshots must not be claimed unless actual files are saved.
