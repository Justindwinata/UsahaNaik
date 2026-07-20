# System Architecture

## Overview

UsahaNaik is an Android app built with Kotlin, Jetpack Compose, Material Design 3, Navigation Compose, ViewModel-ready state boundaries, repository pattern-ready data access, and local-first planning.

UN-0011 adds local reminder planning, Room reminder persistence, Dashboard/Profile reminder UI, and notification-ready architecture with in-app fallback. Real PDF export, remote AI integration, cloud sync, external calendar integration, exact system notification scheduling, and production diagnosis refinement remain planned for later contracts.

## UI Layer

The UI layer uses Jetpack Compose screens and reusable design components:

- App theme, color tokens, typography, and reusable card components.
- Navigation shell with onboarding routes and bottom tabs.
- Dashboard, weekly plan, content ideas, interactive setup, and settings screens.
- Compose-drawn lightweight visual components for progress and trend charts.
- `BusinessSetupViewModel` exposes immutable setup UI state to Compose.
- `FinancialEntryViewModel` exposes immutable financial form, recent activity, validation, and summary state to Compose.
- `DashboardInsightsViewModel` loads saved profile and financial summary, then exposes diagnosis state to Compose.
- `WeeklyPlanViewModel` loads the active plan, generates a new weekly plan, toggles task completion, and exposes progress state to Compose.
- `ContentPlannerViewModel` loads saved profile state, generates content ideas, saves ideas, filters saved ideas, updates favorite/planned/done status, and deletes ideas.
- `ContentCalendarViewModel` schedules saved content ideas, updates local calendar status, and deletes scheduled items.
- `WeeklyRetrospectiveViewModel` generates weekly progress snapshots and deterministic retrospectives from local repositories.
- Dashboard cards can use persisted financial summaries when entries exist.
- Dashboard insight UI renders rule-based score, breakdown, insights, risks, and priority actions.
- Weekly Plan UI renders focus, progress, task checklist, challenge, milestones, and regenerate confirmation.
- Dashboard shows a compact weekly plan summary and CTA to the Weekly Plan tab.
- Dashboard shows a compact content planner summary and CTA to the Content Planner tab.
- Content Planner UI renders generation controls, generated ideas, saved ideas, promotion campaigns, filters, and local-only safety messaging.
- Content Planner UI includes a local content calendar section with planned, posted, skipped, and done status controls.
- Weekly Retrospective UI renders current snapshot, retrospective sections, next-week suggestion, saved history, and progress trend.
- Dashboard shows continuity cards for weekly completion, content execution, latest retrospective, and trend history.
- `BusinessReportViewModel` loads local repositories, generates period-based reports, exposes export-ready text, and saves local report snapshots.
- Business Report UI renders KPI cards, simple visual summaries, finance, growth, diagnosis, content, retrospective, export-ready text, and snapshot history sections.
- Dashboard shows a compact Business Report card and CTA.
- `ReminderViewModel` loads local reminders, validates reminder forms, creates/updates reminders, enables/pauses/deletes reminders, and calls the scheduler abstraction.
- Dashboard shows a compact local reminder summary.
- Settings/Profile renders reminder permission status, reminder form, saved reminder list, and enable/pause/delete actions.
- Settings/Profile exposes Demo Mode controls for loading and clearing local sample data with confirmation dialogs.
- Shared UI components include reusable empty, loading, error, and CTA state cards.
- Settings/Profile can show and delete the saved local business profile.
- Settings/Profile shows the current local-only AI provider mode and documents future API key safety rules.

## Domain Layer

The domain layer contains plain Kotlin models for:

- Business categories.
- Business setup draft and setup enums.
- Business profile domain model with created/updated timestamps.
- Business setup validation result and field identifiers.
- Setup calculations for profit margin, revenue target gap, and profit target gap.
- Financial entries, entry types, income categories, expense categories, and financial tracking summaries.
- Financial calculations for total income, total expenses, estimated profit, profit margin, target progress, largest expense category, recent entries, and chart-ready trend points.
- Dashboard financial metric mapping with saved-entry data and business-profile baseline fallback.
- Business diagnosis models for health score, breakdown, insights, risk signals, priority actions, severity, category, difficulty, and estimated time.
- `BusinessDiagnosisEngine` for deterministic score and insight rules.
- `PriorityActionGenerator` for category-aware and challenge-aware action recommendations.
- Weekly growth plan models for plan, focus, task, challenge, milestone, status, and progress summary.
- `WeeklyPlanGenerator` for deterministic weekly task, challenge, and milestone creation.
- Dashboard weekly plan summary mapping.
- Content planner models for ideas, platforms, goals, types, statuses, sources, promotion campaigns, calendar items, requests, and results.
- Content dashboard summary mapping.
- Content calendar models, status enum, summary calculator, and day grouping.
- Weekly progress snapshot models, metrics, trend points, and history summary.
- `WeeklyProgressSnapshotGenerator` for deterministic continuity snapshots.
- Weekly retrospective models and `WeeklyRetrospectiveGenerator`.
- Dashboard continuity summary mapping.
- Business report models for report periods, KPIs, report chart data, financial summaries, weekly execution, content performance, diagnosis summary, retrospective summary, report insights, export-ready report text, and snapshots.
- `BusinessReportGenerator` for deterministic period-based aggregation across local app data.
- `ExportReadyReportRenderer` for markdown-like copy/share-ready report text with safety disclaimers.
- Business report dashboard summary mapping.
- Business reminder models, reminder time/schedule helpers, permission state, and summary calculation.
- Demo data seeding model for local portfolio presentation.
- Business dashboard preview.
- Financial summary, expense breakdown, and trend points.
- Milestones, tasks, product performance, and recommendations.
- Weekly plans.
- AI-assisted content ideas.
- Business profile/sample summary.
- AI-ready content generation contracts.

Domain models are intentionally independent from Android UI classes.

## Data Layer

The data layer uses local sample and Room-backed repositories:

- `SampleBusinessCategoryRepository`
- `SampleGrowthRepository`
- `LocalContentIdeaProvider`
- `BusinessProfileRepository`
- `LocalBusinessProfileRepository`
- `FinancialEntryRepository`
- `LocalFinancialEntryRepository`
- `UsahaNaikDatabase`
- `BusinessProfileDao`
- `BusinessProfileEntity`
- `FinancialEntryDao`
- `FinancialEntryEntity`
- `WeeklyPlanRepository`
- `LocalWeeklyPlanRepository`
- `WeeklyPlanDao`
- `WeeklyGrowthPlanEntity`
- `WeeklyTaskEntity`
- `WeeklyMilestoneEntity`
- `ContentIdeaRepository`
- `LocalContentIdeaRepository`
- `ContentIdeaDao`
- `ContentIdeaEntity`
- `ContentCalendarRepository`
- `LocalContentCalendarRepository`
- `ContentCalendarDao`
- `ContentCalendarEntity`
- `WeeklyProgressHistoryRepository`
- `LocalWeeklyProgressHistoryRepository`
- `WeeklyProgressSnapshotDao`
- `WeeklyProgressSnapshotEntity`
- `WeeklyRetrospectiveRepository`
- `LocalWeeklyRetrospectiveRepository`
- `WeeklyRetrospectiveDao`
- `WeeklyRetrospectiveEntity`
- `BusinessReportSnapshotRepository`
- `LocalBusinessReportSnapshotRepository`
- `BusinessReportSnapshotDao`
- `BusinessReportSnapshotEntity`
- `BusinessReminderRepository`
- `LocalBusinessReminderRepository`
- `BusinessReminderDao`
- `BusinessReminderEntity`
- `DemoDataSeeder`

Room stores one active business profile in `usahanaik.db`, table `business_profiles`, simple local financial records in `financial_entries`, one active weekly plan across `weekly_growth_plans`, `weekly_tasks`, and `weekly_milestones`, saved content ideas in `content_ideas`, local content schedules in `content_calendar_items`, weekly progress snapshots in `weekly_progress_snapshots`, weekly retrospectives in `weekly_retrospectives`, saved report snapshots in `business_report_snapshots`, and local reminder definitions in `business_reminders`. Multi-business support is deferred.

UN-0003 saves completed setup data locally and reloads it on app startup. UN-0004 saves income and expense entries locally and maps monthly entry summaries into dashboard cards and trend visuals.

UN-0005 does not add new Room tables. It reads the saved business profile and financial summary through repository interfaces, then generates diagnosis output in the domain layer.

UN-0006 updates Room to version 3 with additive weekly plan tables. Replacing/regenerating the active weekly plan deletes the previous active plan rows and saves the new plan, tasks, and milestones.

UN-0007 updates Room to version 4 with an additive `content_ideas` table. Generated content ideas can be saved, filtered, favorited, marked planned, marked done, deleted, or cleared locally.

UN-0008 updates Room to version 7 through additive migrations. Version 5 adds `content_calendar_items`, version 6 adds `weekly_progress_snapshots`, and version 7 adds `weekly_retrospectives`.

UN-0009 updates Room to version 8 with an additive `business_report_snapshots` table. Report snapshots store period, business name, generated date, headline summary, export-ready text, health score, revenue, expenses, estimated profit, task completion rate, content execution rate, and metadata timestamps.

UN-0010 does not change the Room schema. Demo Mode uses existing repositories and tables to insert a sample dataset after confirmation.

UN-0011 updates Room to version 9 with an additive `business_reminders` table. Reminders store title, description, type, frequency, scheduled day/date labels, time label, status, optional related entity id, and metadata timestamps.

Planned data direction:

- Repository interfaces expose app data.
- Local Room implementations persist completed setup profile data.
- Financial entries are persisted as simple local records, not as a full accounting ledger.
- Diagnosis is calculated in memory from local data and is not persisted yet.
- Weekly plan task completion is persisted locally.
- Milestone progress is persisted and refreshed from related task completion where practical.
- Content idea status and favorite state are persisted locally.
- Content calendar status is persisted locally.
- Weekly progress snapshots are persisted locally and can replace an existing snapshot for the same week.
- Weekly retrospectives are persisted locally and can replace an existing retrospective for the same week.
- Report snapshots are persisted locally as export-ready report history.
- Reminder definitions are persisted locally and summarized for Dashboard/Profile.
- Sample repositories remain useful for previews and tests.

## Reminder And Notification Architecture

UN-0011 adds notification-ready reminder architecture without implementing exact OS alarm/work scheduling yet.

Components:

- `BusinessReminder` models local reminder definitions.
- `ReminderSummaryCalculator` creates Dashboard/Profile counts and next-reminder copy.
- `BusinessReminderRepository` hides Room persistence from UI.
- `ReminderViewModel` owns form state, validation, status updates, scheduler calls, and summary state.
- `ReminderPermissionHelper` checks Android notification permission state.
- `ReminderNotificationManager` creates the local reminder notification channel.
- `ReminderMessageFactory` creates safe reminder copy without guaranteed business outcomes.
- `ReminderScheduler` abstracts scheduling and cancellation.
- `AndroidReminderScheduler` currently prepares channel/permission behavior and returns in-app fallback results.

Current limitation:

- Reminders are persisted and visible in-app.
- System notification execution is architecture/skeleton only in UN-0011.
- Exact scheduling through AlarmManager or WorkManager is deferred until device/emulator QA can verify permission granted and denied flows.

## AI Integration Planned

UN-0001 defines an AI-ready boundary through a content idea provider interface. UN-0007 expands this boundary into a safe provider architecture while keeping the shipped app deterministic and local.

Current contract:

- `ContentIdeaProvider` defines the AI boundary.
- `LocalContentIdeaProvider` returns deterministic category-aware and challenge-aware ideas.
- `ConfigurableAiContentIdeaProvider` is a remote-provider skeleton that requires future user-provided settings before any real integration.
- `FallbackContentIdeaProvider` catches provider failure and returns local fallback ideas.
- `ContentIdeaPromptBuilder` builds a structured, safe prompt without secrets.
- `AiContentSettings` models local-only or AI-assisted modes without storing any key in source code.
- No API key, paid AI dependency, or external request is used in UN-0007.

Content ideas are labeled as suggestions that must be reviewed before posting. The app avoids guaranteed sales/profit claims, fake scarcity, and unsupported sensitive claims.

Future AI integration should:

- Avoid hardcoded API keys.
- Use secure configuration.
- Clearly label generated suggestions.
- Keep disclaimers around non-guaranteed recommendations.
- Allow deterministic fallback samples for offline/test mode.
- Never log user API keys.

## Local-First Direction

The app works without login. User setup data is persisted locally with Room before any cloud sync is considered.

Current local-first behavior:

- Completed setup can be saved locally.
- Saved profile can be restored on app startup.
- Dashboard can use saved business data.
- Dashboard can use saved financial entries for monthly revenue, expenses, estimated profit, margin, target progress, recent entries, and trend visuals.
- Dashboard can generate rule-based business diagnosis insights from local profile and financial records.
- Weekly plans can be generated, saved, restored, and progressed locally.
- Dashboard can show active weekly plan progress.
- Content ideas can be generated, saved, filtered, favorited, planned, completed, and deleted locally.
- Dashboard can show content planning progress.
- Saved content ideas can be scheduled in an internal local content calendar.
- Weekly progress snapshots and retrospectives can be generated and saved locally.
- Dashboard can show continuity cards from weekly plan, calendar, retrospective, and snapshot history state.
- Business reports can be generated from local data, previewed in the app, and saved as local snapshots.
- Dashboard can show a compact Business Report summary.
- Demo data can be loaded and cleared locally from Settings/Profile.
- Local reminders can be created, enabled, paused, deleted, and shown on Dashboard/Profile.
- Settings/Profile can delete the saved local profile.
- No authentication or cloud sync is used.
