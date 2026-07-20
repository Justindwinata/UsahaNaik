# UX Polish And Demo Readiness Plan

## Goal

UN-0010 prepares UsahaNaik as a portfolio-ready Android demo. The app should feel like one cohesive local-first UMKM business planner, not separate feature experiments.

## Product Tone

- Warm, practical, and dashboard-oriented.
- Clear enough for non-technical UMKM owners.
- Safe wording: rule-based insights, estimated financial summary, local planning support.
- Avoid guaranteed profit, official accounting, tax-ready, or professional financial advice claims.

## Screen Audit

### Welcome And Onboarding

- Keep the current direct entry path.
- Make saved-profile resume and demo-readiness messaging clearer.
- Avoid long explanatory copy in primary flow.

### Category Selection

- Preserve pastel category cards.
- Keep selection state visible with a text badge, not color only.
- Ensure selected category hints remain easy to scan.

### Business Setup

- Keep one scrollable form.
- Improve section-level helper wording and validation hierarchy where needed.
- Keep review action clear and avoid implying guaranteed outcomes.

### Dashboard

- Treat dashboard as the main business command center.
- Prioritize business health, report summary, weekly plan, financial metrics, content status, and continuity.
- Reduce the feeling of a long unrelated list by using clearer section cards and CTAs.
- Show limited-data prompts with concrete next actions.

### Financial Tracking

- Keep the form compact.
- Make empty recent activity state actionable.
- Preserve estimated profit language.

### Weekly Plan

- Keep generated plan, challenge, task, and milestone sections.
- Make empty state strongly guide the user to generate a plan.
- Ensure task statuses are visible by text label, not only checkbox state.

### Content Planner And Calendar

- Keep local/fallback generation clearly labeled.
- Make generated ideas, saved ideas, calendar schedules, and promotion campaigns feel connected.
- Add clearer empty states and next action prompts.

### Retrospective

- Emphasize weekly learning and progress continuity.
- Keep deterministic/heuristic wording.
- Make history and empty state easy to understand.

### Business Report

- Keep period selector prominent.
- Make KPI cards and simple chart bars readable on mobile.
- Keep export-ready text preview clear and disclaimer visible.
- Make saved snapshots feel like local report history.

### Settings/Profile

- Add Demo Mode controls.
- Keep local data and AI-provider safety copy visible.
- Make reset/delete actions explicit and confirm destructive behavior.

## Demo Mode Plan

Add a local demo data seeder that populates:

- Business profile: `Dapur Rasa Nusantara`, Food & Beverage, mixed online/offline.
- Financial entries: realistic income and expense records.
- Weekly plan: active plan with completed and pending tasks.
- Content ideas: saved, favorite, planned, and done ideas.
- Content calendar: planned, posted, skipped, and done scheduled content.
- Weekly progress snapshot.
- Weekly retrospective.
- Business report snapshot.

Settings/Profile will expose:

- `Load Demo Data` with confirmation that current local app data may be replaced.
- `Clear Demo Data` using existing local repositories with confirmation.
- Success copy: `Demo data loaded. Open Dashboard to explore UsahaNaik.`

The seeder should avoid duplicate demo data by clearing relevant local records before inserting the sample dataset.

## Shared UI Polish Plan

- Add reusable empty, loading, error, and CTA cards.
- Improve touch targets for shared buttons.
- Add semantic content descriptions where useful.
- Keep cards rounded, pastel, compact, and readable.
- Avoid color-only status by using labels and badges.

## QA Risks

- Demo seeding touches many repositories; tests must cover duplicate prevention and complete dataset creation.
- Dashboard refresh can call several ViewModels; avoid infinite recomposition loops.
- Room schema is version 8; demo mode should use repositories rather than changing schema.
- Report snapshots are local history, not official accounting/tax exports.
- Manual emulator QA may not be available; if no device is attached, final notes must say so.

## Acceptance Criteria

- App builds and unit tests pass.
- Demo data can be loaded through Settings/Profile.
- Dashboard, Weekly Plan, Content Planner, Retrospective, and Business Report have data-rich demo states.
- Empty/loading/error states are clearer across core screens.
- Documentation explains demo flow, screenshot plan, QA checklist, and portfolio positioning.
