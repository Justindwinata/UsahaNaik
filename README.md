# UsahaNaik

UsahaNaik is an Android portfolio project for Indonesian UMKM owners. The app is planned as an AI-assisted business growth planner that helps users structure business data, review financial signals, follow weekly action plans, monitor milestones, and preview creative content ideas.

The app does not claim guaranteed profit, guaranteed business success, or professional financial advice.

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
