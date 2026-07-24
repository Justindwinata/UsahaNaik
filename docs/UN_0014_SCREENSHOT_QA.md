# UN-0014 Screenshot QA Audit

## Evidence Source

Manual emulator screenshots were reported by the user and summarized in the UN-0014 contract. No screenshot image files were found in the repository at audit time; only `UI_Reference.jpg` is present locally. This document therefore records the user-provided screenshot findings as runtime evidence and tracks the fixes made during UN-0014.

Fresh screenshots should be captured under `docs/evidence/screenshots/un0014/` if an emulator or physical device is available during final QA. Do not claim screenshot capture unless files are actually created.

## Screenshot-Based Issues Observed

1. Some content appears clipped near the top/status-bar area.
2. Bottom navigation visually competes with content and needs clearer spacing.
3. Cards are too large and too text-heavy on medium phone size.
4. Important actions require too much scrolling.
5. Pastel colors are friendly but sometimes reduce hierarchy.
6. Typography is occasionally oversized or inconsistent.
7. Muted gray text needs stronger contrast.
8. Indonesian and English labels are mixed inconsistently.
9. Dashboard has useful sections but needs clearer priority.
10. Diagnosis and report sections need easier scanning and less repeated copy.
11. Financial tracking needs stronger validation and feedback.
12. Weekly plan task and milestone progress need clearer feedback.
13. Content Planner needs a stronger generate-save-schedule workflow.
14. Profile/reminder settings need clearer permission and saved-reminder states.
15. Demo Mode should feel deliberate and reliable.
16. Local-first, auth-ready, and AI-ready wording must remain accurate.

## Screen-By-Screen Audit

### Welcome / Login / Register

- Risk: Auth-ready copy can be misunderstood as real login/register.
- Fix plan: Keep local-mode CTA prominent, use concise placeholder copy, and never imply password persistence or backend accounts.

### Dashboard

- Risk: The dashboard can feel like a long feed instead of a command center.
- Fix plan: Improve top safe area, add compact hierarchy, reduce repeated disclaimers, make KPI and action cards easier to scan, and ensure finance updates refresh metrics.

### Financial Tracking

- Risk: Amount and title entry can accept confusing input; save feedback may not be obvious.
- Fix plan: Harden validation, make amount parsing friendlier, preserve useful defaults, show success/error state, and keep recent entries visible.

### Weekly Plan

- Risk: Task completion and milestone progress may not feel immediate.
- Fix plan: Add clearer task-progress feedback, compact task cards, and explicit regenerate behavior.

### Retrospective

- Risk: Retrospective can be text-heavy and unclear when no snapshot/history exists.
- Fix plan: Improve empty, success, and history states, and ensure copy remains heuristic/planning-oriented.

### Content Planner

- Risk: Generation controls are tall and the generated/saved/scheduled idea path is not obvious.
- Fix plan: Compact controls, clarify local deterministic generation, improve generated/saved/calendar states, and keep scheduling feedback visible.

### Business Report

- Risk: Report can feel cluttered and too text-heavy.
- Fix plan: Improve section hierarchy, KPI scanability, chart explanation, and export-ready disclaimer placement.

### Profile / Language / Demo / Reminders

- Risk: Settings can feel like a utility dump.
- Fix plan: Group language, auth placeholder, local profile, demo mode, reminders, AI settings, and reset controls with clearer cards and concise labels.

### Navigation

- Risk: Bottom navigation can cover content or compete visually.
- Fix plan: Apply safe content padding, use stable tabs, keep auth routes outside bottom nav, and document route QA.

## Planned Fix Areas

- Safe-area and bottom-navigation layout.
- Design system contrast, typography, button, badge, and card refinements.
- Bilingual product copy cleanup.
- Dashboard command-center hierarchy.
- Financial tracking validation and feedback.
- Weekly plan progress clarity.
- Retrospective saved-state clarity.
- Content planner generate/save/schedule flow.
- Profile/reminder/demo mode reliability.
- Navigation continuity and QA documentation.

## Fixed Areas

This section should be updated as commits land during UN-0014.

- Safe-area pass added additional top breathing room and bottom scroll padding for shared screen containers.
- Auth entry screens now use full-height background and stronger top/bottom padding so first content is less likely to clip.
- Design-system pass softened secondary pastel fills, strengthened muted text contrast, reduced default card radius/padding, normalized primary button height, and made badges more compact.
- Bilingual copy pass localized shared empty/error/preview/action badges, dashboard hero labels, KPI helper text, and core Content Planner section labels.
- Dashboard hierarchy pass moved business health and diagnosis above quick actions, removed duplicate KPI cards, and consolidated legacy product/action/content previews into one compact business-signals card.

## Remaining Manual QA Items

- Capture fresh screenshots on Medium Phone emulator.
- Verify top content is not clipped.
- Verify bottom navigation does not hide content.
- Verify language persists after app restart.
- Verify local-mode auth entry routes.
- Verify financial entries update dashboard metrics.
- Verify weekly tasks update progress.
- Verify retrospective generation/save.
- Verify content idea generation/save/schedule.
- Verify reminders can be created, paused, and listed.
- Verify demo data load and clear flows.
