# UsahaNaik

UsahaNaik is an Android portfolio project for Indonesian UMKM owners. The app is planned as an AI-assisted business growth planner that helps users structure business data, review financial signals, follow weekly action plans, monitor milestones, and preview creative content ideas.

The app does not claim guaranteed profit, guaranteed business success, or professional financial advice.

## Target Users

UsahaNaik is designed for Indonesian UMKM owners such as warung owners, food and beverage sellers, skincare and beauty sellers, online shop owners, laundry businesses, coffee shops, service businesses, freelancers, and home-based businesses.

## Feature Vision

The long-term product vision includes business setup, category-specific planning, health score insights, revenue and expense dashboards, weekly action plans, milestones, tasks, content ideas, promotion ideas, education ideas, financial summaries, and next-action recommendations.

## Current Scope

UN-0002 adds the first real interactive business setup workflow on top of the UN-0001 Android foundation.

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

## Tech Stack

- Kotlin
- Jetpack Compose
- Material Design 3
- Navigation Compose
- ViewModel-ready architecture
- Repository pattern-ready structure
- Room planned for later
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

- No full business form persistence yet.
- Setup draft is not permanently saved yet.
- No Room database yet.
- No real AI API integration yet.
- Content ideas are local preview samples only.
- No full business diagnosis engine yet.
- No weekly plan generation from real diagnosis yet.
- No authentication, cloud sync, payment, or notification system.
- No guaranteed profit increase.
- Not professional financial advice.

## Roadmap

Next recommended milestone: UN-0003, Room persistence for business setup drafts, goals, and local dashboard data.

## Documentation

- [Product Requirements](docs/PRODUCT_REQUIREMENTS.md)
- [System Architecture](docs/SYSTEM_ARCHITECTURE.md)
- [Roadmap](docs/ROADMAP.md)
- [Decision Log](docs/DECISION_LOG.md)
- [UI Reference Analysis](docs/UI_REFERENCE_ANALYSIS.md)
