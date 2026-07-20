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

Completed scope:

- Add business diagnosis domain models.
- Add deterministic health score and score breakdown.
- Generate financial, goal progress, challenge, content, stock, and risk insights.
- Generate 3-5 category-aware priority actions.
- Connect dashboard insight state through a ViewModel.
- Render score, insight summary, insight cards, priority actions, and risk/attention cards.
- Keep diagnosis transparent, heuristic, and non-guaranteed.

## UN-0006 - Weekly Planning Engine

Generate structured weekly plans from business category, challenges, and goals using deterministic local logic.

Completed scope:

- Add weekly growth plan, task, challenge, milestone, and progress models.
- Generate deterministic weekly focus, 5-7 tasks, one challenge, and 3-5 milestones.
- Use saved profile, diagnosis, category, challenges, available time, and financial summary as inputs.
- Persist one active weekly plan locally with Room.
- Persist task completion and milestone progress.
- Render Weekly Plan screen with progress, checklist tasks, challenge, milestones, and regenerate confirmation.
- Add dashboard weekly plan summary.
- Keep plan recommendations transparent, heuristic, and non-guaranteed.

## UN-0007 - AI Content Provider Integration

Add safe configurable AI provider integration without hardcoded keys, while preserving local fallback mode.

Completed scope:

- Add content planner domain models for ideas, platforms, goals, types, statuses, generation source, promotion campaigns, calendar items, requests, and results.
- Upgrade local deterministic content generation with category-aware, challenge-aware, and platform-aware ideas.
- Generate hook, caption draft, CTA, visual suggestion, posting note, and safety note for each idea.
- Add promotion campaign suggestions with safe expected-outcome wording.
- Add optional AI provider architecture with settings model, prompt builder, configurable provider skeleton, and local fallback wrapper.
- Persist saved content ideas locally with Room.
- Render Content Planner screen with generation controls, generated ideas, saved ideas, status filters, favorite/planned/done/delete actions, and promotion planner cards.
- Add dashboard content planning summary.
- Add local-only AI provider information in Settings/Profile.
- Keep remote AI calls and API key storage deferred.

## UN-0008 - Milestones And Progress

Add milestone completion, progress history, content calendar scheduling, streaks, and plan retrospectives.

Completed scope:

- Add local content calendar models, summary helpers, Room persistence, repository, and scheduling UI.
- Let saved content ideas be scheduled with date, optional time label, posting note, platform, and local status.
- Support planned, posted, skipped, and done status for scheduled content.
- Add weekly progress snapshot models, deterministic generator, Room persistence, repository, and dashboard trend mapping.
- Add weekly retrospective models, deterministic generator, Room persistence, repository, route, and screen.
- Add dashboard continuity cards for weekly completion, content execution, latest retrospective, and progress trend.
- Keep notifications, external calendar integration, and cloud sync deferred.

## UN-0009 - Notifications

Add optional local reminders for sales tracking, weekly reviews, content planning, and retrospective follow-up.

## UN-0010 - Export And Reports

Add local financial and progress report export for owner review.

## UN-0011 - Portfolio Polish

Improve animations, accessibility, UI tests, screenshots, README assets, and release-readiness documentation.
