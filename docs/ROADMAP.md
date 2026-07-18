# Roadmap

## UN-0001 - Android Project Bootstrap And Product Foundation

Create Android foundation, documentation, Compose shell, navigation, sample dashboard, weekly plan preview, and local content idea preview.

## UN-0002 - Interactive Business Setup

Add real form input, validation, temporary state handling, and category-specific setup guidance.

Completed scope:

- Persist screen state in a ViewModel.
- Validate required business setup fields.
- Let selected category influence setup hints.
- Keep persistence in memory for this milestone unless the form model is stable enough for Room.
- Add setup review.
- Personalize dashboard preview from valid draft state.

## UN-0003 - Room Persistence

Persist business profile, financial baseline, goals, tasks, milestones, and content idea history locally.

Completed scope:

- Add Room entities for business setup draft and goals.
- Persist setup draft locally.
- Restore setup state after app process recreation.
- Keep migrations simple and covered by tests.
- Avoid cloud sync until local-first behavior is stable.
- Add local profile delete/reset action.
- Restore dashboard from saved profile data.

## UN-0004 - Financial Tracking

Add revenue, expense, profit, margin, and basic transaction tracking with local reports.

Completed scope:

- Add local financial entry models and Room tables.
- Track revenue and expense entries.
- Connect dashboard financial cards to persisted entries.
- Add recent financial activity and delete confirmation.
- Add target progress, largest expense category, and chart-ready summary data.
- Keep calculations transparent and avoid professional financial advice claims.

## UN-0005 - Dashboard Insights

Add deterministic insight cards from saved profile data, selected challenges, category hints, and financial activity.

## UN-0006 - Weekly Planning Engine

Generate structured weekly plans from business category, challenges, and goals using deterministic local logic.

## UN-0007 - AI Content Provider Integration

Add safe configurable AI provider integration without hardcoded keys, while preserving local fallback mode.

## UN-0008 - Milestones And Progress

Add milestone completion, progress history, streaks, and plan retrospectives.

## UN-0009 - Notifications

Add optional local reminders for sales tracking, weekly reviews, and content planning.

## UN-0010 - Export And Reports

Add local financial and progress report export for owner review.

## UN-0011 - Portfolio Polish

Improve animations, accessibility, UI tests, screenshots, README assets, and release-readiness documentation.
