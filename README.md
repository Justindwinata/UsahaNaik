# UsahaNaik

UsahaNaik is an Android portfolio project for Indonesian UMKM owners. The app is planned as an AI-assisted business growth planner that helps users structure business data, review financial signals, follow weekly action plans, monitor milestones, and preview creative content ideas.

The app does not claim guaranteed profit, guaranteed business success, or professional financial advice.

## Target Users

UsahaNaik is designed for Indonesian UMKM owners such as warung owners, food and beverage sellers, skincare and beauty sellers, online shop owners, laundry businesses, coffee shops, service businesses, freelancers, and home-based businesses.

## Feature Vision

The long-term product vision includes business setup, category-specific planning, health score insights, revenue and expense dashboards, weekly action plans, milestones, tasks, content ideas, promotion ideas, education ideas, financial summaries, and next-action recommendations.

## Current Scope

UN-0001 establishes the Android project foundation, Compose UI shell, navigation direction, local sample data, and product documentation.

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

## Build And Test

```bash
./gradlew tasks
./gradlew test
./gradlew assembleDebug
```

## Limitations

- No full business form persistence yet.
- No Room database yet.
- No real AI API integration yet.
- Content ideas are local preview samples only.
- No authentication, cloud sync, payment, or notification system.

## Documentation

- [Product Requirements](docs/PRODUCT_REQUIREMENTS.md)
- [System Architecture](docs/SYSTEM_ARCHITECTURE.md)
- [Roadmap](docs/ROADMAP.md)
- [Decision Log](docs/DECISION_LOG.md)
- [UI Reference Analysis](docs/UI_REFERENCE_ANALYSIS.md)
