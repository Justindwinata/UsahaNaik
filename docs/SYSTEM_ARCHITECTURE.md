# System Architecture

## Overview

UsahaNaik is an Android app built with Kotlin, Jetpack Compose, Material Design 3, Navigation Compose, ViewModel-ready state boundaries, repository pattern-ready data access, and local-first planning.

UN-0001 focuses on product foundation and UI shell. Persistence, real AI integration, and production data flows are planned for later contracts.

## UI Layer

The UI layer uses Jetpack Compose screens and reusable design components:

- App theme, color tokens, typography, and reusable card components.
- Navigation shell with onboarding routes and bottom tabs.
- Dashboard, weekly plan, content ideas, setup preview, and settings screens.
- Compose-drawn lightweight visual components for progress and trend charts.

## Domain Layer

The domain layer contains plain Kotlin models for:

- Business categories.
- Business profile/sample summary.
- Financial metrics.
- Milestones.
- Weekly tasks.
- Content ideas.
- AI-ready content generation contracts.

Domain models are intentionally independent from Android UI classes.

## Data Layer

UN-0001 uses local sample repositories. Room is planned later for persistent business setup data, weekly plans, financial entries, content ideas, and progress history.

Planned data direction:

- Repository interfaces expose app data.
- Local Room implementations persist user input.
- Sample repositories remain useful for previews and tests.

## AI Integration Planned

UN-0001 defines an AI-ready boundary through a content idea provider interface. The current implementation is deterministic and local.

Future AI integration should:

- Avoid hardcoded API keys.
- Use secure configuration.
- Clearly label generated suggestions.
- Keep disclaimers around non-guaranteed recommendations.
- Allow deterministic fallback samples for offline/test mode.

## Local-First Direction

The app should work without login in early milestones. User data persistence should start locally with Room before any cloud sync is considered.
