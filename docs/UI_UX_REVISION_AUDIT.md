# UI/UX Revision Audit

## Audit Inputs

- Screenshot folder check: no current app screenshots were found in the repository.
- Available image: `UI_Reference.jpg`, which shows a polished soft-card mobile dashboard pattern.
- Code inspected:
  - `app/src/main/java/com/justindwinata/usahanaik/ui/UsahaNaikApp.kt`
  - `app/src/main/java/com/justindwinata/usahanaik/ui/navigation/AppRoutes.kt`
  - `app/src/main/java/com/justindwinata/usahanaik/ui/screens/PlaceholderScreens.kt`
  - `app/src/main/java/com/justindwinata/usahanaik/ui/components/UsahaNaikCards.kt`
  - `app/src/main/java/com/justindwinata/usahanaik/ui/theme/Color.kt`

Because runtime screenshots were not available, this audit is based on the current Compose implementation, app flow, existing shared components, and the provided reference image.

## Reference Direction

The reference image uses:

- Soft off-white backgrounds.
- Compact pastel cards.
- Clear top header with profile/action affordances.
- Strong metric hierarchy.
- Rounded navigation bar.
- Simple charts and progress indicators.
- Friendly spacing and lightweight shadows.

UsahaNaik should keep this softness but translate it into a business dashboard instead of a health dashboard.

## Global Problems

### Visual Hierarchy

The current app has many feature sections stacked vertically. Several screens read like a long internal prototype rather than a designed product surface. Primary actions, secondary actions, status labels, warnings, and data cards often compete for attention.

Improvement plan:

- Introduce a stronger screen container with title, subtitle, optional badge, and primary action.
- Group dashboard content into command-center blocks: business status, money, execution, content, report, reminders.
- Make every major screen start with a concise purpose and one obvious next action.

### Spacing And Alignment

Spacing exists through `AppSpacing`, but the long screen file has many local layout decisions. This creates uneven rhythm between cards, forms, chips, and CTA blocks.

Improvement plan:

- Standardize card padding, section spacing, grid spacing, and button height.
- Use shared wrappers for screen body, action rows, KPI grids, and empty states.
- Avoid ad hoc card nesting.

### Typography

Current typography is readable but not yet differentiated enough for a polished dashboard. Some cards use similar text sizes for title, value, helper copy, and CTA text.

Improvement plan:

- Strengthen display hierarchy for hero titles, dashboard KPI values, card titles, body helper copy, and small metadata.
- Keep compact components using smaller titles and avoid hero-scale text inside cards.

### Color And Contrast

The palette is warm and friendly, but repeated pastel cards can feel visually flat. Some warning/success states rely heavily on color.

Improvement plan:

- Keep cream/orange/green as core identity.
- Add darker neutral surfaces, subtle borders, and status text labels.
- Ensure every status has text, not only color.

### Component Consistency

Shared components exist, but screen-specific code still contains many one-off layouts and hardcoded labels.

Improvement plan:

- Add reusable professional components: screen container, KPI card, status badge, language selector, auth field, CTA panel, and compact progress panel.
- Use these components across entry, dashboard, profile, and high-traffic flows first.

### Navigation

The main bottom navigation currently has Dashboard, Plan, Ideas, and Profile. Business Report is reachable as a secondary route from Dashboard. There is no auth-ready entry route yet. Language selection is not represented in navigation or state.

Improvement plan:

- Add `login` and `register` entry routes.
- Keep bottom navigation uncluttered.
- Add Report as a clear dashboard CTA or bottom tab if space allows after UX review.
- Keep Finance under Dashboard, Calendar under Ideas, Retrospective under Plan/Report, Reminders under Profile.

### Localization

Most static UI copy is hardcoded in Compose and mostly English. This makes bilingual support risky if handled with search-and-replace.

Improvement plan:

- Add a supported-language model with Indonesian default.
- Add a localized copy provider for high-visibility UI copy.
- Persist selected language locally.
- Start with static shell, entry, dashboard, setup, finance, plan, ideas, report, reminders, and profile labels. Generated deterministic content can remain partially local/dynamic.

### Maintainability

`PlaceholderScreens.kt` contains nearly 4,000 lines and all major screen implementations. This makes UI quality harder to control.

Improvement plan:

- Avoid a broad risky file split during this revision.
- Add shared components and localization boundaries first.
- Move future large screens into dedicated files incrementally after visual behavior stabilizes.

## Screen Audit

### Welcome

Visible UI problems:

- Entry screen is not auth-ready.
- It does not yet establish language choice as a first-class setup step.
- Product positioning and local-first limitation copy need a more professional first impression.

UX flow problems:

- User can start setup or preview dashboard, but login/register/local-mode expectations are missing.

Improvement plan:

- Redesign welcome as a polished entry with app name, tagline, language selector, login/register actions, and continue-local-mode CTA.
- Add safe copy that authentication is not enabled in this local-first demo version.

### Login

Current state:

- Screen does not exist.

Improvement plan:

- Add email/password UI placeholder.
- Add login button placeholder, continue local mode, register link, and language switch.
- Do not store password or create fake account persistence.

### Register

Current state:

- Screen does not exist.

Improvement plan:

- Add name/email/password/confirm-password UI placeholder.
- Add register button placeholder, continue local mode, login link, and language switch.
- Do not implement real auth.

### Category Selection

Visible UI problems:

- Category cards likely work functionally but need clearer business-owner guidance and stronger selected state.

UX flow problems:

- Should feel like a guided setup step rather than a generic list.

Improvement plan:

- Add step header, progress context, category hints, and localized labels.
- Make the selected category visually clear with text and icon/badge state.

### Business Setup

Visible UI problems:

- Large multi-section form can feel dense.
- Validation is functional but needs stronger guided-step visual hierarchy.

UX flow problems:

- The review action should feel like a deliberate transition to creating a personalized dashboard.

Improvement plan:

- Add stepper/progress card.
- Use section completion indicators.
- Improve grouped form cards, helper copy, and review summary.
- Localize major labels and CTAs.

### Dashboard

Visible UI problems:

- Dashboard has many features and can become a long unorganized feed.
- Metrics, insights, finance form, reports, reminders, and continuity cards compete for priority.

UX flow problems:

- Dashboard should be the main command center, not just a container for every feature.

Improvement plan:

- Restructure into top business header, KPI grid, health score, quick actions, finance summary, weekly execution, content summary, report CTA, and reminder summary.
- Use compact dashboard cards and consistent section headers.
- Keep the finance entry form available but visually secondary to summary metrics.

### Finance

Visible UI problems:

- Finance currently lives inside Dashboard and may feel like a form inserted into a report page.

UX flow problems:

- Users need a clear path: record entry, see recent entries, return to dashboard metrics.

Improvement plan:

- Polish income/expense toggle, amount field, category chips, and recent entries.
- Improve empty state: record first income/expense to make the dashboard more accurate.

### Weekly Plan

Visible UI problems:

- Plan contains useful data but needs stronger card hierarchy for focus, progress, tasks, challenge, and milestones.

UX flow problems:

- Regenerate and retrospective actions need clearer impact and placement.

Improvement plan:

- Add a stronger focus card and progress panel.
- Make task status and milestone progress visually consistent.
- Keep retrospective CTA under progress context.

### Content Planner

Visible UI problems:

- Generation controls, saved ideas, calendar, and promotion planner can feel overloaded.

UX flow problems:

- Users need an obvious sequence: choose goal/platform, generate, save, schedule, mark progress.

Improvement plan:

- Create a clearer generation control card.
- Use idea cards with consistent hook/caption/CTA sections.
- Keep calendar as a section under saved ideas.

### Retrospective

Visible UI problems:

- Retrospective content can be text-heavy.

UX flow problems:

- User needs to understand whether they are generating a new review or reading history.

Improvement plan:

- Add current-week summary first, then generated sections, then history.
- Make the next-week suggestion visually prominent.

### Business Report

Visible UI problems:

- Report screen needs to feel like a business output, not only a debug aggregation page.

UX flow problems:

- Period selector, KPIs, charts, export-ready text, and snapshots need clearer sections.

Improvement plan:

- Use a professional report header and KPI grid.
- Keep charts simple but intentional.
- Add disclaimer near export-ready summary.

### Profile / Settings

Visible UI problems:

- Profile contains many utilities: business profile, demo mode, reminders, AI settings, local data. It risks becoming a settings dump.

UX flow problems:

- Language, local mode/auth placeholder, reminders, and reset actions need clearer grouping.

Improvement plan:

- Add profile header and account-placeholder card.
- Add language switch near the top.
- Group local profile, demo data, reminders, AI settings, and danger-zone reset controls.

### Reminders

Visible UI problems:

- Reminder permission and fallback states need polished messaging.

UX flow problems:

- User should understand that reminders work in-app even without notification permission.

Improvement plan:

- Keep permission card user-triggered.
- Add active/paused status labels.
- Keep next reminder summary visible in Dashboard and Profile.

## Revision Priorities

1. Add bilingual language architecture and persistence.
2. Add auth-ready welcome/login/register entry.
3. Strengthen shared design system components.
4. Redesign Dashboard and Business Setup first.
5. Polish Plan, Ideas, Report, Retrospective, Profile, and Reminders.
6. Harden navigation and local-mode flow.
7. Update portfolio docs and QA checklist.

## Non-Goals

- Real authentication.
- Backend server.
- Cloud sync.
- Real AI API calls.
- Payment features.
- PDF export.
- Guaranteed profit or professional financial advice claims.
