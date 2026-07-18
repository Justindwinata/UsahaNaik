# System Architecture

## Overview

UsahaNaik is an Android app built with Kotlin, Jetpack Compose, Material Design 3, Navigation Compose, ViewModel-ready state boundaries, repository pattern-ready data access, and local-first planning.

UN-0002 adds interactive setup state, validation, review, and dashboard draft mapping. Persistence, real AI integration, and production data flows are planned for later contracts.

## UI Layer

The UI layer uses Jetpack Compose screens and reusable design components:

- App theme, color tokens, typography, and reusable card components.
- Navigation shell with onboarding routes and bottom tabs.
- Dashboard, weekly plan, content ideas, interactive setup, and settings screens.
- Compose-drawn lightweight visual components for progress and trend charts.
- `BusinessSetupViewModel` exposes immutable setup UI state to Compose.

## Domain Layer

The domain layer contains plain Kotlin models for:

- Business categories.
- Business setup draft and setup enums.
- Business setup validation result and field identifiers.
- Setup calculations for profit margin, revenue target gap, and profit target gap.
- Business dashboard preview.
- Financial summary, expense breakdown, and trend points.
- Milestones, tasks, product performance, and recommendations.
- Weekly plans.
- AI-assisted content ideas.
- Business profile/sample summary.
- AI-ready content generation contracts.

Domain models are intentionally independent from Android UI classes.

## Data Layer

UN-0001 uses local sample repositories:

- `SampleBusinessCategoryRepository`
- `SampleGrowthRepository`
- `LocalContentIdeaProvider`

Room is planned later for persistent business setup data, weekly plans, financial entries, content ideas, and progress history.

UN-0002 keeps setup data in memory through `BusinessSetupViewModel`. The dashboard can map a valid draft into preview data, but the draft is not permanently saved.

Planned data direction:

- Repository interfaces expose app data.
- Local Room implementations persist user input.
- Sample repositories remain useful for previews and tests.

## AI Integration Planned

UN-0001 defines an AI-ready boundary through a content idea provider interface. The current implementation is deterministic and local.

Current contract:

- `ContentIdeaProvider` defines the AI boundary.
- `LocalContentIdeaProvider` returns deterministic sample ideas.
- No API key, paid AI dependency, or external request is used in UN-0001.

UN-0002 does not change the AI boundary. Content ideas remain local/sample-based.

Future AI integration should:

- Avoid hardcoded API keys.
- Use secure configuration.
- Clearly label generated suggestions.
- Keep disclaimers around non-guaranteed recommendations.
- Allow deterministic fallback samples for offline/test mode.

## Local-First Direction

The app should work without login in early milestones. User data persistence should start locally with Room before any cloud sync is considered.
