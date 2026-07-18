# UsahaNaik

UsahaNaik is an Android portfolio project for Indonesian UMKM owners. The app is planned as an AI-assisted business growth planner that helps users structure business data, review financial signals, follow weekly action plans, monitor milestones, and preview creative content ideas.

The app does not claim guaranteed profit, guaranteed business success, or professional financial advice.

## Target Users

UsahaNaik is designed for Indonesian UMKM owners such as warung owners, food and beverage sellers, skincare and beauty sellers, online shop owners, laundry businesses, coffee shops, service businesses, freelancers, and home-based businesses.

## Feature Vision

The long-term product vision includes business setup, category-specific planning, health score insights, revenue and expense dashboards, weekly action plans, milestones, tasks, content ideas, promotion ideas, education ideas, financial summaries, and next-action recommendations.

## Current Scope

UN-0004 adds simple local financial tracking so UMKM owners can record income and expenses and see dashboard metrics update from saved Room data.

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

## Tech Stack

- Kotlin
- Jetpack Compose
- Material Design 3
- Navigation Compose
- ViewModel-ready architecture
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
- Content ideas are local preview samples only.
- No full business diagnosis engine yet.
- No weekly plan generation from real diagnosis yet.
- No authentication, cloud sync, payment, or notification system.
- No guaranteed profit increase.
- Not professional accounting software.
- Not professional financial advice.

## Roadmap

Next recommended milestone: UN-0005, dashboard insights and clearer deterministic business signals from saved profile and financial activity.

## Documentation

- [Product Requirements](docs/PRODUCT_REQUIREMENTS.md)
- [System Architecture](docs/SYSTEM_ARCHITECTURE.md)
- [Roadmap](docs/ROADMAP.md)
- [Decision Log](docs/DECISION_LOG.md)
- [UI Reference Analysis](docs/UI_REFERENCE_ANALYSIS.md)
