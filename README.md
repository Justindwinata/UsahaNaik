# UsahaNaik

Local-first Android business growth planner for Indonesian UMKM owners.

UsahaNaik is an Android portfolio project that helps users structure business data, track simple finance, review rule-based insights, follow weekly action plans, plan content, schedule content locally, generate retrospectives, and review business reports.

The app does not claim guaranteed profit, guaranteed business success, or professional financial advice.

## Key Features

- Interactive business setup with category-driven hints.
- Local Room persistence for one active business profile.
- Simple income and expense tracking with estimated financial summaries.
- Rule-based business diagnosis and business health score.
- Weekly growth plan, task completion, challenge, and milestone tracking.
- Content planner with deterministic local content ideas and optional AI-ready fallback architecture.
- Local content calendar with planned, posted, skipped, and done status.
- Weekly progress snapshots and deterministic retrospectives.
- Business Report dashboard with KPI cards, simple Compose visual summaries, export-ready text, and local report snapshots.
- Demo Mode for portfolio presentation using sample UMKM data.
- Local reminder planning for daily finance tracking, weekly tasks, scheduled content, retrospectives, and report review.

## Target Users

UsahaNaik is designed for Indonesian UMKM owners such as warung owners, food and beverage sellers, skincare and beauty sellers, online shop owners, laundry businesses, coffee shops, service businesses, freelancers, and home-based businesses.

## Feature Vision

The long-term product vision includes business setup, category-specific planning, health score insights, revenue and expense dashboards, weekly action plans, milestones, tasks, content ideas, promotion ideas, education ideas, financial summaries, and next-action recommendations.

## Current Scope

UN-0011 adds local reminder planning and notification-ready architecture. Reminders are persisted locally, visible in Dashboard/Profile, included in Demo Mode, and designed to work as in-app fallback even when Android notification permission is unavailable.

Demo Mode sample business:

- Business: Dapur Rasa Nusantara.
- Category: Food & Beverage.
- Includes sample profile, finance entries, weekly plan, content ideas, content calendar schedules, progress snapshot, retrospective, report snapshot, and local reminders.

To load demo data in the app:

1. Open `Profile`.
2. Tap `Load Demo Data`.
3. Confirm that local app data may be replaced.
4. Return to Dashboard, Weekly Plan, Content Planner, Retrospective, or Business Report.

Screenshot files are not committed yet. See [Screenshot Plan](docs/SCREENSHOT_PLAN.md) for the capture checklist.

Implemented UN-0001 screens:

- Welcome / app entry.
- Business category selection.
- Business setup form preview.
- Growth dashboard preview.
- Weekly plan preview.
- Content ideas preview.
- Profile / settings placeholder.

Implemented UN-0001 foundation:

- Compose design system inspired by `UI_Reference.jpg`.
- Navigation Compose routes and post-onboarding bottom navigation.
- Business category metadata for 10 UMKM categories.
- Dashboard sample data for health score, financial cards, trend chart, expense breakdown, milestones, tasks, product performance, recommendations, and content ideas.
- AI-ready `ContentIdeaProvider` interface with local deterministic provider.
- Unit tests for category metadata and local content idea generation.

Implemented UN-0002 foundation:

- Category selection feeds the setup draft state.
- Interactive multi-section business setup form.
- Business identity, financial baseline, product/service data, challenges, and monthly goals.
- Friendly inline validation for required fields and non-negative numeric inputs.
- Indonesian-style numeric parsing for values such as `Rp 18.000.000`, `18,5 juta`, and `250 rb`.
- Category-driven setup hints and recommended monthly focus.
- In-memory `BusinessSetupViewModel` draft state.
- Setup review summary with profit margin, revenue gap, and profit gap.
- Dashboard preview can use valid draft values.
- Unit tests for validation, state reducers, hints, and dashboard draft mapping.

Implemented UN-0003 foundation:

- Room database configured with KSP.
- Single active business profile stored in `business_profiles`.
- Local repository abstraction for save, read, observe, delete, and has-profile checks.
- Entity/domain mappers for setup draft, enums, challenges, and timestamps.
- Setup review saves the completed business profile locally.
- App startup loads the saved profile into dashboard-ready state.
- Welcome screen can resume a saved local profile.
- Dashboard preview can use persisted business data.
- Settings/Profile shows saved profile summary and local-first data notice.
- Settings/Profile can delete the saved local profile with confirmation.
- Unit tests cover mapping, repository behavior, save action, restore action, and delete action.

Implemented UN-0004 foundation:

- Financial entry domain models for income and expense records.
- Simple UMKM-friendly income and expense categories.
- Room `financial_entries` table with DAO, migration, repository, and entity/domain mappers.
- Financial calculation helpers for total income, total expenses, estimated profit, profit margin, target progress, largest expense category, recent entries, and trend points.
- Dashboard financial entry form with type selector, category chips, date, note, validation, local save, recent activity, and delete confirmation.
- Dashboard cards and trend chart can use persisted monthly financial entries when available.
- Empty dashboard financial state falls back to the saved business profile baseline and prompts the user to start recording entries.
- Unit tests cover calculation helpers, persistence/repository behavior, ViewModel validation/save/delete, and dashboard metric mapping.

Implemented UN-0005 foundation:

- Business diagnosis domain models for health score, score breakdown, insights, risk signals, priority actions, severities, categories, difficulty, and estimated time.
- Deterministic `BusinessDiagnosisEngine` that reviews saved profile data and financial summaries.
- Rule-based business health score with financial clarity, profitability, goal progress, expense control, execution readiness, and category fit components.
- Insight rules for no financial entries, positive profit, negative profit, high expense ratio, target progress, poor financial records, low sales, inconsistent content, and stock issues.
- Category-aware priority actions for Food & Beverage, Warung / Toko Kelontong, Skincare & Beauty, Online Shop / Reseller, Laundry, and fallback business cases.
- `DashboardInsightsViewModel` that loads saved profile and financial summary, then exposes diagnosis state to Compose.
- Dashboard UI for rule-based score breakdown, insight summary cards, business insight cards, risk/attention cards, and 3-5 priority action cards.
- Unit tests cover scoring rules, insights, risks, category actions, state integration, and dashboard mapping behavior.

Implemented UN-0006 foundation:

- Weekly growth plan domain models for plan, focus, task, challenge, milestone, status, and progress summary.
- Deterministic `WeeklyPlanGenerator` that uses saved profile, diagnosis, challenges, category, available time, and financial summary.
- Rule-based weekly focus selection for financial records, expenses, sales, content, repeat orders, stock, risks, and general execution.
- 5-7 weekly tasks with category, estimated time, difficulty, reason, and non-guaranteed expected outcome wording.
- One weekly challenge and 3-5 milestones per generated plan.
- Room persistence for `weekly_growth_plans`, `weekly_tasks`, and `weekly_milestones` with database migration to version 3.
- `WeeklyPlanViewModel` for loading, generating, regenerating, saving, toggling task completion, and progress tracking.
- Weekly Plan screen with focus card, progress card, checklist task cards, challenge card, milestone cards, and regenerate confirmation.
- Dashboard weekly plan summary with active focus, task progress, milestone progress, next task, and CTA to the Weekly Plan tab.
- Unit tests cover generator rules, persistence, mapping, ViewModel behavior, and dashboard summary mapping.

Implemented UN-0007 foundation:

- Content planner domain models for ideas, platforms, goals, types, status, generation source, promotion campaigns, calendar items, requests, and results.
- Improved deterministic `LocalContentIdeaProvider` with category-aware, challenge-aware, platform-aware ideas.
- Optional AI provider architecture with settings model, prompt builder, configurable provider skeleton, and safe fallback wrapper.
- No hardcoded API keys, no paid AI dependency, and no required network call.
- Room `content_ideas` table with DAO, migration, repository, and entity/domain mappers.
- Content Planner screen with generation controls, generated idea cards, saved ideas, status filters, favorite/planned/done/delete actions, and promotion campaign cards.
- Dashboard content summary with saved, planned, done, favorite, next idea, and CTA.
- Settings/Profile local-only AI provider section documenting planned configuration and key safety rules.
- Tests cover local generation, fallback behavior, prompt safety, persistence, ViewModel state, and dashboard content mapping.

Implemented UN-0008 foundation:

- Content calendar domain models, status enum, day grouping, and summary calculation.
- Room `content_calendar_items` table with DAO, migration, repository, and entity/domain mapper.
- Content Planner scheduling flow for saved ideas with date, optional time label, posting note, platform, status controls, and delete action.
- Weekly progress snapshot models and deterministic generator for task, milestone, finance, content, and diagnosis continuity metrics.
- Room `weekly_progress_snapshots` table with replace-current-week repository behavior.
- Weekly retrospective domain models and deterministic generator for improved items, attention items, completed tasks, missed tasks, content summary, financial summary, and next-week suggestion.
- Room `weekly_retrospectives` table with save, latest, history, and replace-current-week behavior.
- Retrospective route and screen accessible from Weekly Plan.
- Dashboard continuity section with weekly progress, content calendar, latest retrospective, and progress trend cards.
- Tests cover calendar summary, calendar persistence, calendar ViewModel, snapshot generation, snapshot persistence, retrospective generation, retrospective persistence, and dashboard continuity mapping.

Implemented UN-0009 foundation:

- Business report domain models for periods, KPIs, financial summary, chart data, content performance, weekly execution, diagnosis summary, retrospective summary, insights, export-ready report text, and report snapshots.
- Deterministic `BusinessReportGenerator` that aggregates local business profile, financial entries, diagnosis, weekly plan, content ideas, content calendar schedules, progress snapshots, and latest retrospective.
- Export-ready markdown-like text report with financial, business health, weekly execution, content activity, retrospective, recommendation, and disclaimer sections.
- Room `business_report_snapshots` table with DAO, migration to database version 8, repository, mapper, and tests.
- `BusinessReportViewModel` for period selection, report generation, export text exposure, snapshot saving, and saved snapshot history.
- Business Report route and Compose screen with period selector, KPI cards, simple bar-style visual summaries, report sections, export-ready text preview, and save snapshot action.
- Dashboard Business Report card with estimated profit, health score, task completion, content execution, and CTA.
- Tests cover report models, generator period filtering, export renderer safety, snapshot persistence, ViewModel state, and dashboard summary mapping.

Implemented UN-0010 foundation:

- UX polish plan and QA risk audit.
- Shared empty, loading, error, and CTA state components.
- Dashboard command-center framing and report state polish.
- Cleaner Weekly Plan, Content Planner, Content Calendar, and Retrospective empty/error states.
- Local Demo Mode with confirmation dialogs in Settings/Profile.
- Demo data seeder for a realistic Food & Beverage UMKM sample.
- Route-entry refresh hooks so demo data appears when opening core screens.
- Bottom navigation accessibility descriptions.
- Portfolio documentation, demo script, QA checklist, screenshot plan, and showcase summary.

Implemented UN-0011 foundation:

- Local reminder domain models for daily finance tracking, weekly plan tasks, scheduled content, weekly retrospective, and business report review.
- Room `business_reminders` table with migration to database version 9.
- Reminder repository and mapper for local create, update, enable, pause, delete, list, active list, and summary behavior.
- Notification-ready scheduler architecture with permission helper, notification channel setup, message factory, and scheduler abstraction.
- In-app fallback behavior when notification permission is unavailable.
- Profile reminder management UI with permission status, reminder form, saved reminder list, enable/pause/delete actions, empty/loading/error states, and status labels.
- Dashboard reminder summary card with active count, paused count, next reminder, and permission state.
- Demo Mode now seeds sample reminders for Dapur Rasa Nusantara.
- Unit tests cover reminder models, mapping, repository behavior, scheduler fallback, ViewModel state, and demo reminder seeding.

## Tech Stack

- Kotlin
- Jetpack Compose
- Material Design 3
- Navigation Compose
- ViewModel architecture
- Repository pattern-ready structure
- Room Database
- Kotlin Coroutines
- JUnit

## Local Setup

Open this repository in Android Studio and let Gradle sync the project. Android SDK 35 is used for compilation, with minimum SDK 26.

If running from terminal, make sure the Android SDK path is available. On this machine the validation command used:

```bash
ANDROID_HOME=/Users/justindwinata/Library/Android/sdk ./gradlew test
ANDROID_HOME=/Users/justindwinata/Library/Android/sdk ./gradlew assembleDebug
```

## Build And Test

```bash
./gradlew tasks
./gradlew test
./gradlew assembleDebug
```

If Gradle cannot find the SDK, either open the project in Android Studio or set `ANDROID_HOME` to your local Android SDK path.

## Limitations

- Local persistence only.
- Only one active business profile is supported.
- Financial entries are simple local records, not a full accounting ledger.
- No real AI API integration yet.
- Content ideas are deterministic local suggestions unless a future optional AI provider is configured.
- Diagnosis is deterministic and heuristic.
- Weekly plan is deterministic and heuristic.
- Content calendar is local only.
- Progress history is deterministic.
- Weekly retrospective is heuristic and deterministic.
- Business reports are generated from local app data only.
- Business report snapshots are saved locally.
- Charts are simple Compose visual summaries.
- Demo data is sample local data for presentation only.
- No PDF export yet.
- AI provider architecture is present, but remote AI generation is not implemented in this milestone.
- No hardcoded API keys.
- Generated content ideas should be reviewed before posting.
- Reminder system notifications are architecture-ready, but exact OS alarm/work scheduling is not implemented yet.
- Notification behavior requires device/emulator QA.
- No external calendar integration.
- No authentication, cloud sync, or payment system.
- No guaranteed profit increase.
- Not professional accounting software.
- Not professional financial advice.

## Roadmap

Next recommended milestone: UN-0012, notification execution and permission QA with AlarmManager or WorkManager after the local reminder architecture is stable.

## Documentation

- [Product Requirements](docs/PRODUCT_REQUIREMENTS.md)
- [System Architecture](docs/SYSTEM_ARCHITECTURE.md)
- [Roadmap](docs/ROADMAP.md)
- [Decision Log](docs/DECISION_LOG.md)
- [UI Reference Analysis](docs/UI_REFERENCE_ANALYSIS.md)
- [Demo Script](docs/DEMO_SCRIPT.md)
- [QA Checklist](docs/QA_CHECKLIST.md)
- [Screenshot Plan](docs/SCREENSHOT_PLAN.md)
- [Portfolio Showcase](docs/PORTFOLIO_SHOWCASE.md)
