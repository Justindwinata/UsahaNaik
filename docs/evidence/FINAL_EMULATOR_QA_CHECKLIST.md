# Final Emulator QA Checklist

Use this checklist only for real emulator/device testing. Do not mark an item complete unless it was manually verified.

## Device

- [ ] Emulator/device name:
- [ ] Android version:
- [ ] App build:
- [ ] Fresh install performed:

## Entry And Language

- [ ] Welcome opens without clipped content.
- [ ] Indonesian is available.
- [ ] English is available.
- [ ] Language switch persists after app restart.
- [ ] Login placeholder opens.
- [ ] Register placeholder opens.
- [ ] Continue local mode routes correctly.

## Dashboard

- [ ] Dashboard top content is not clipped.
- [ ] Bottom navigation does not cover content.
- [ ] Business health card is readable.
- [ ] Financial metric source label is visible.
- [ ] Empty states show clear next actions.
- [ ] Demo data populates Dashboard.

## Finance

- [ ] Income entry saves locally.
- [ ] Expense entry saves locally.
- [ ] Invalid amount is rejected.
- [ ] Invalid date is rejected.
- [ ] Dashboard metrics update after save.
- [ ] Recent entries render.

## Weekly Plan And Retrospective

- [ ] Weekly Plan opens.
- [ ] Plan generation/regeneration works.
- [ ] Task completion updates count.
- [ ] Milestone progress remains coherent.
- [ ] Retrospective generates from local state.
- [ ] Retrospective saves and history appears.

## Ideas And Calendar

- [ ] Content ideas generate locally.
- [ ] Generated idea can be saved.
- [ ] Saved idea appears in Saved Ideas.
- [ ] Filters work.
- [ ] Idea can be scheduled.
- [ ] Calendar section updates.

## Report

- [ ] Report opens.
- [ ] Period selector works.
- [ ] KPI cards render.
- [ ] Export-ready text renders.
- [ ] Snapshot save works if tested.

## Profile, Reminders, Demo Mode

- [ ] Profile opens.
- [ ] Reminder with valid `HH:mm` saves.
- [ ] Reminder with `99:99` is rejected.
- [ ] Active/paused reminder state persists.
- [ ] Notification permission UI does not crash.
- [ ] Load Demo Data works.
- [ ] Clear Demo Data works.

## Screenshot Evidence

- [ ] Screenshots captured.
- [ ] Screenshots saved under `docs/evidence/screenshots/final/`.
- [ ] No screenshot claim is made without actual files.
