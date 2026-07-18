# UsahaNaik

UsahaNaik is an Android portfolio project for Indonesian UMKM owners. The app is planned as an AI-assisted business growth planner that helps users structure business data, review financial signals, follow weekly action plans, monitor milestones, and preview creative content ideas.

The app does not claim guaranteed profit, guaranteed business success, or professional financial advice.

## Target Users

UsahaNaik is designed for Indonesian UMKM owners such as warung owners, food and beverage sellers, skincare and beauty sellers, online shop owners, laundry businesses, coffee shops, service businesses, freelancers, and home-based businesses.

## Feature Vision

The long-term product vision includes business setup, category-specific planning, health score insights, revenue and expense dashboards, weekly action plans, milestones, tasks, content ideas, promotion ideas, education ideas, financial summaries, and next-action recommendations.

## Current Scope

UN-0007 adds a real Content Planner with deterministic local content idea generation, saved ideas, status tracking, promotion campaign suggestions, and optional AI provider architecture without hardcoded secrets.

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
- AI provider architecture is present, but remote AI generation is not implemented in this milestone.
- No hardcoded API keys.
- Generated content ideas should be reviewed before posting.
- No authentication, cloud sync, payment, or notification system.
- No guaranteed profit increase.
- Not professional accounting software.
- Not professional financial advice.

## Roadmap

Next recommended milestone: UN-0008, milestone progress history, content calendar scheduling, and portfolio polish around progress continuity.

## Documentation

- [Product Requirements](docs/PRODUCT_REQUIREMENTS.md)
- [System Architecture](docs/SYSTEM_ARCHITECTURE.md)
- [Roadmap](docs/ROADMAP.md)
- [Decision Log](docs/DECISION_LOG.md)
- [UI Reference Analysis](docs/UI_REFERENCE_ANALYSIS.md)
