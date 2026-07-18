# System Architecture

## Overview

UsahaNaik is an Android app built with Kotlin, Jetpack Compose, Material Design 3, Navigation Compose, ViewModel-ready state boundaries, repository pattern-ready data access, and local-first planning.

UN-0004 adds local financial entry persistence and dashboard metrics from saved income and expense records. Real AI integration, cloud sync, and production diagnosis flows remain planned for later contracts.

## UI Layer

The UI layer uses Jetpack Compose screens and reusable design components:

- App theme, color tokens, typography, and reusable card components.
- Navigation shell with onboarding routes and bottom tabs.
- Dashboard, weekly plan, content ideas, interactive setup, and settings screens.
- Compose-drawn lightweight visual components for progress and trend charts.
- `BusinessSetupViewModel` exposes immutable setup UI state to Compose.
- `FinancialEntryViewModel` exposes immutable financial form, recent activity, validation, and summary state to Compose.
- Dashboard cards can use persisted financial summaries when entries exist.
- Settings/Profile can show and delete the saved local business profile.

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

Room stores one active business profile in `usahanaik.db`, table `business_profiles`, and simple local financial records in `financial_entries`. Multi-business support is deferred.

UN-0003 saves completed setup data locally and reloads it on app startup. UN-0004 saves income and expense entries locally and maps monthly entry summaries into dashboard cards and trend visuals.

Planned data direction:

- Repository interfaces expose app data.
- Local Room implementations persist completed setup profile data.
- Financial entries are persisted as simple local records, not as a full accounting ledger.
- Sample repositories remain useful for previews and tests.

## AI Integration Planned

UN-0001 defines an AI-ready boundary through a content idea provider interface. The current implementation is deterministic and local.

Current contract:

- `ContentIdeaProvider` defines the AI boundary.
- `LocalContentIdeaProvider` returns deterministic sample ideas.
- No API key, paid AI dependency, or external request is used in UN-0001.

UN-0004 does not change the AI boundary. Content ideas remain local/sample-based.

Future AI integration should:

- Avoid hardcoded API keys.
- Use secure configuration.
- Clearly label generated suggestions.
- Keep disclaimers around non-guaranteed recommendations.
- Allow deterministic fallback samples for offline/test mode.

## Local-First Direction

The app works without login. User setup data is persisted locally with Room before any cloud sync is considered.

Current local-first behavior:

- Completed setup can be saved locally.
- Saved profile can be restored on app startup.
- Dashboard can use saved business data.
- Dashboard can use saved financial entries for monthly revenue, expenses, estimated profit, margin, target progress, recent entries, and trend visuals.
- Settings/Profile can delete the saved local profile.
- No authentication or cloud sync is used.
