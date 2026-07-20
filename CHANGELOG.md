# Changelog

## 0.9.0 - UN-0009

- Added business report domain models for report periods, KPI cards, chart data, financial summaries, weekly execution, content performance, diagnosis summaries, retrospective highlights, export-ready text, and report snapshots.
- Added deterministic `BusinessReportGenerator` that aggregates local profile, finance, diagnosis, weekly plan, content planner, content calendar, progress history, and retrospective data.
- Added export-ready markdown-like report renderer with safe disclaimer text and no guaranteed outcome claims.
- Added Room `business_report_snapshots` persistence with DAO, migration to database version 8, repository, mapper, and tests.
- Added `BusinessReportViewModel` for period selection, report refresh, chart-ready state, export-ready text, snapshot save, and saved snapshot history.
- Added Business Report route and Compose report dashboard with KPI grid, simple visual charts, financial summary, growth progress, diagnosis, content performance, retrospective, and export-ready summary sections.
- Added dashboard Business Report card with estimated profit, health score, task completion, content execution, and CTA.
- Added tests for report models, generator behavior, export renderer safety, snapshot persistence, ViewModel state, and dashboard summary mapping.

### Known Limitations

- Report is generated from local app data only.
- Report is not an official accounting or tax document.
- No PDF export yet.
- Charts are simple Compose visual summaries.
- No cloud sync.
- No authentication.
- No real AI API integration yet.
- No guaranteed profit increase.
- Not professional financial advice.

## 0.8.0 - UN-0008

- Added local content calendar domain models, status enum, day grouping, and summary calculations.
- Added Room `content_calendar_items` persistence with DAO, migration, repository, and mapper.
- Added Content Planner scheduling flow for saved ideas with date, optional time label, posting note, platform, status controls, and delete action.
- Added weekly progress snapshot domain models and deterministic generator.
- Added Room `weekly_progress_snapshots` persistence with replace-current-week behavior.
- Added weekly retrospective domain models and deterministic generator.
- Added Room `weekly_retrospectives` persistence with save, latest, history, and replace-current-week behavior.
- Added Retrospective route and screen from Weekly Plan.
- Added dashboard continuity cards for weekly completion, content calendar execution, latest retrospective, and progress trend.
- Added tests for calendar models, calendar persistence, calendar ViewModel, snapshot generation/persistence, retrospective generation/persistence, and dashboard continuity mapping.

### Known Limitations

- Content calendar is local only.
- No Android notifications yet.
- No external calendar integration.
- Progress history is deterministic.
- Retrospective is heuristic.
- No real AI API integration yet.
- Local persistence only.
- No cloud sync.
- No authentication.
- Not professional financial advice.
- No guaranteed profit increase.

## 0.7.0 - UN-0007

- Added content planner domain models for content ideas, platforms, goals, types, statuses, generation sources, promotion campaigns, calendar items, requests, and results.
- Upgraded deterministic local content generation with category-aware and challenge-aware ideas for UMKM business types.
- Added platform-specific hooks, caption drafts, CTA suggestions, visual suggestions, posting notes, and safety notes.
- Added promotion campaign suggestions with objective, recommended content sequence, expected outcome wording, and safety note.
- Added optional AI provider architecture with local fallback, settings model, prompt builder, and configurable provider skeleton.
- Added Room `content_ideas` persistence with DAO, migration, repository, and entity/domain mapper.
- Added `ContentPlannerViewModel` for loading profile state, generating ideas, saving ideas, filtering, favorite toggles, planned/done status, and delete actions.
- Replaced the static content ideas preview with a real Content Planner screen.
- Added dashboard content summary with saved, planned, done, favorite, next idea, and CTA.
- Added Settings/Profile AI provider section explaining local-only mode and key safety rules for future configuration.
- Added tests for local provider behavior, fallback behavior, prompt safety, persistence, ViewModel state, and dashboard content summary mapping.

### Known Limitations

- Local content generation is deterministic.
- Remote AI generation is not implemented yet.
- No hardcoded API keys.
- No guaranteed content performance.
- No guaranteed sales or profit increase.
- Content ideas should be reviewed before posting.
- Local persistence only.
- No cloud sync.
- No authentication.
- No notification system.

## 0.6.0 - UN-0006

- Added weekly growth plan domain models for focus, task, challenge, milestone, status, and progress summary.
- Added deterministic `WeeklyPlanGenerator` using saved profile, financial summary, diagnosis, challenges, category, goals, and available time.
- Added rule-based weekly focus selection for financial tracking, expense control, sales activity, content consistency, repeat orders, stock control, and general execution.
- Added generated weekly challenges and 3-5 milestones.
- Added Room `weekly_growth_plans`, `weekly_tasks`, and `weekly_milestones` persistence with migration to database version 3.
- Added `WeeklyPlanRepository` and `LocalWeeklyPlanRepository`.
- Added `WeeklyPlanViewModel` for loading, generating, regenerating, saving, toggling task completion, and deleting active plans.
- Replaced the static Weekly Plan preview with a state-driven weekly plan screen.
- Added dashboard weekly plan summary with focus, task progress, milestone progress, next task, and CTA.
- Added tests for generator rules, mapper behavior, repository persistence, ViewModel state, and dashboard summary mapping.

### Known Limitations

- Weekly plan is deterministic and heuristic.
- Task recommendations are planning suggestions.
- Not professional financial advice.
- No guaranteed profit increase.
- Local persistence only.
- No cloud sync.
- No authentication.
- No real AI API integration yet.
- Content ideas are still local/sample-based.
- No notification system.

## 0.5.0 - UN-0005

- Added business diagnosis domain models for health score, score breakdown, insights, risk signals, priority actions, severities, categories, difficulty, and estimated time.
- Added deterministic `BusinessDiagnosisEngine` for rule-based diagnosis from saved profile data and financial summaries.
- Added score components for financial clarity, profitability, goal progress, expense control, execution readiness, and category fit.
- Added insight and risk rules for no records, positive profit, negative profit, high expenses, target progress, poor financial records, low sales, inconsistent content, and stock issues.
- Added category-aware `PriorityActionGenerator` with practical UMKM actions and 3-5 action output.
- Added `DashboardInsightsViewModel` to load saved profile and financial summary, generate diagnosis, and expose dashboard insight state.
- Added dashboard UI for rule-based health score, score breakdown, insight summary, insight cards, priority action cards, and risk/attention cards.
- Added tests for diagnosis rules, priority action generation, ViewModel state, and model behavior.

### Known Limitations

- Diagnosis is deterministic and heuristic.
- Not professional financial advice.
- No guaranteed profit increase.
- Local persistence only.
- No cloud sync.
- No authentication.
- No real AI API integration yet.
- Content ideas are still local/sample-based.
- Weekly plan is not fully generated from diagnosis yet.
- No notification system.

## 0.4.0 - UN-0004

- Added financial entry domain models for income and expense tracking.
- Added simple UMKM-friendly income and expense categories.
- Added deterministic financial calculations for total income, total expenses, estimated profit, profit margin, target progress, largest expense category, recent entries, and trend points.
- Added Room `financial_entries` persistence with DAO, migration, repository, and entity/domain mappers.
- Added `FinancialEntryViewModel` with form state, validation, save action, delete confirmation state, and summary refresh.
- Added dashboard financial tracking UI for recording local income and expense entries.
- Added recent financial activity cards with delete confirmation.
- Updated dashboard revenue, expense, profit, margin, target progress, largest expense category, and trend chart to use persisted financial entries when available.
- Kept saved business profile baseline as the dashboard fallback when no financial entries exist.
- Added tests for financial calculations, mapping, repository behavior, ViewModel behavior, and dashboard metric mapping.

### Known Limitations

- Local persistence only.
- No cloud sync.
- No authentication.
- No real AI API integration yet.
- Content ideas are still local/sample-based.
- No full business diagnosis engine yet.
- No weekly plan generation from real diagnosis yet.
- No notification system.
- Not professional accounting software.
- No guaranteed profit increase.
- Not professional financial advice.

## 0.3.0 - UN-0003

- Added Room runtime, Room KTX, Room compiler, Room testing, and KSP configuration.
- Added `UsahaNaikDatabase`, `BusinessProfileEntity`, and `BusinessProfileDao`.
- Added `BusinessProfileRepository` and `LocalBusinessProfileRepository`.
- Added entity/domain mapping for setup drafts, enums, selected challenges, and metadata timestamps.
- Added save profile action from setup review.
- Added saved profile loading on app startup.
- Added saved profile resume card on welcome screen.
- Updated dashboard preview to prefer persisted profile data when available.
- Added Settings/Profile local data summary and delete confirmation.
- Added tests for mapping, repository save/read/update/delete, ViewModel save/load/delete, and dashboard mapping.

### Known Limitations

- Local persistence only.
- No cloud sync.
- No authentication.
- No real AI API integration yet.
- Content ideas are still local/sample-based.
- No full business diagnosis engine yet.
- No weekly plan generation from real diagnosis yet.
- No notification system.
- No guaranteed profit increase.
- Not professional financial advice.

## 0.2.0 - UN-0002

- Added `BusinessSetupDraft` domain model and setup enums for channel, stage, cost driver, stock issue, challenges, focus, and available time.
- Added deterministic setup validation with friendly field errors.
- Added Indonesian-style numeric parsing and derived calculations for profit margin, revenue target gap, and profit target gap.
- Added `BusinessSetupViewModel` and immutable `BusinessSetupUiState`.
- Added category-driven setup hints and recommended monthly focus metadata.
- Replaced setup placeholder with an interactive multi-section Compose form.
- Added challenge multi-select chips, selector chips, section completion indicators, and reset draft action.
- Added setup review section with financial summary and target gaps.
- Updated dashboard preview to use valid in-memory draft data when available.
- Added unit tests for validation, ViewModel state behavior, category hints, and dashboard draft mapping.

### Known Limitations

- No Room persistence yet.
- Setup draft is not permanently saved yet.
- No real AI API integration yet.
- Content ideas are still local/sample-based.
- No full business diagnosis engine yet.
- No weekly plan generation from real diagnosis yet.
- No notification system.
- No authentication.
- No cloud sync.
- No guaranteed profit increase.
- Not professional financial advice.

## 0.1.0 - UN-0001

- Initialized Android project foundation for UsahaNaik.
- Added baseline Gradle, Compose, Material 3, and Navigation Compose configuration.
- Added initial project documentation placeholders.
- Documented product requirements, architecture, roadmap, decision log, and UI reference analysis.
- Added Compose design system with warm UMKM dashboard colors, cards, badges, progress, metric, and chart components.
- Added Navigation Compose app shell with onboarding routes and bottom navigation.
- Added 10 business categories with metadata and setup form preview.
- Added dashboard preview, weekly plan preview, and AI-assisted content idea preview using local sample data.
- Added AI-ready `ContentIdeaProvider` interface and deterministic local provider.
- Added unit tests for category metadata and local content idea generation.

### Known Limitations

- No full business form persistence yet.
- No Room database yet.
- No real AI API integration yet.
- Content ideas are local preview samples.
- No real financial report persistence yet.
- No notification system yet.
- No authentication.
- No cloud sync.
- No guaranteed profit increase.
- Not professional financial advice.
