# System Architecture

## Overview

UsahaNaik is an Android app built with Kotlin, Jetpack Compose, Material Design 3, Navigation Compose, ViewModel-ready state boundaries, repository pattern-ready data access, and local-first planning.

UN-0007 adds deterministic content planning, saved content ideas, promotion campaign suggestions, and optional AI provider architecture with local fallback. Real remote AI integration, cloud sync, and production diagnosis refinement remain planned for later contracts.

## UI Layer

The UI layer uses Jetpack Compose screens and reusable design components:

- App theme, color tokens, typography, and reusable card components.
- Navigation shell with onboarding routes and bottom tabs.
- Dashboard, weekly plan, content ideas, interactive setup, and settings screens.
- Compose-drawn lightweight visual components for progress and trend charts.
- `BusinessSetupViewModel` exposes immutable setup UI state to Compose.
- `FinancialEntryViewModel` exposes immutable financial form, recent activity, validation, and summary state to Compose.
- `DashboardInsightsViewModel` loads saved profile and financial summary, then exposes diagnosis state to Compose.
- `WeeklyPlanViewModel` loads the active plan, generates a new weekly plan, toggles task completion, and exposes progress state to Compose.
- `ContentPlannerViewModel` loads saved profile state, generates content ideas, saves ideas, filters saved ideas, updates favorite/planned/done status, and deletes ideas.
- Dashboard cards can use persisted financial summaries when entries exist.
- Dashboard insight UI renders rule-based score, breakdown, insights, risks, and priority actions.
- Weekly Plan UI renders focus, progress, task checklist, challenge, milestones, and regenerate confirmation.
- Dashboard shows a compact weekly plan summary and CTA to the Weekly Plan tab.
- Dashboard shows a compact content planner summary and CTA to the Content Planner tab.
- Content Planner UI renders generation controls, generated ideas, saved ideas, promotion campaigns, filters, and local-only safety messaging.
- Settings/Profile can show and delete the saved local business profile.
- Settings/Profile shows the current local-only AI provider mode and documents future API key safety rules.

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
- Business diagnosis models for health score, breakdown, insights, risk signals, priority actions, severity, category, difficulty, and estimated time.
- `BusinessDiagnosisEngine` for deterministic score and insight rules.
- `PriorityActionGenerator` for category-aware and challenge-aware action recommendations.
- Weekly growth plan models for plan, focus, task, challenge, milestone, status, and progress summary.
- `WeeklyPlanGenerator` for deterministic weekly task, challenge, and milestone creation.
- Dashboard weekly plan summary mapping.
- Content planner models for ideas, platforms, goals, types, statuses, sources, promotion campaigns, calendar items, requests, and results.
- Content dashboard summary mapping.
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
- `WeeklyPlanRepository`
- `LocalWeeklyPlanRepository`
- `WeeklyPlanDao`
- `WeeklyGrowthPlanEntity`
- `WeeklyTaskEntity`
- `WeeklyMilestoneEntity`
- `ContentIdeaRepository`
- `LocalContentIdeaRepository`
- `ContentIdeaDao`
- `ContentIdeaEntity`

Room stores one active business profile in `usahanaik.db`, table `business_profiles`, simple local financial records in `financial_entries`, one active weekly plan across `weekly_growth_plans`, `weekly_tasks`, and `weekly_milestones`, and saved content ideas in `content_ideas`. Multi-business support is deferred.

UN-0003 saves completed setup data locally and reloads it on app startup. UN-0004 saves income and expense entries locally and maps monthly entry summaries into dashboard cards and trend visuals.

UN-0005 does not add new Room tables. It reads the saved business profile and financial summary through repository interfaces, then generates diagnosis output in the domain layer.

UN-0006 updates Room to version 3 with additive weekly plan tables. Replacing/regenerating the active weekly plan deletes the previous active plan rows and saves the new plan, tasks, and milestones.

UN-0007 updates Room to version 4 with an additive `content_ideas` table. Generated content ideas can be saved, filtered, favorited, marked planned, marked done, deleted, or cleared locally.

Planned data direction:

- Repository interfaces expose app data.
- Local Room implementations persist completed setup profile data.
- Financial entries are persisted as simple local records, not as a full accounting ledger.
- Diagnosis is calculated in memory from local data and is not persisted yet.
- Weekly plan task completion is persisted locally.
- Milestone progress is persisted and refreshed from related task completion where practical.
- Content idea status and favorite state are persisted locally.
- Sample repositories remain useful for previews and tests.

## AI Integration Planned

UN-0001 defines an AI-ready boundary through a content idea provider interface. UN-0007 expands this boundary into a safe provider architecture while keeping the shipped app deterministic and local.

Current contract:

- `ContentIdeaProvider` defines the AI boundary.
- `LocalContentIdeaProvider` returns deterministic category-aware and challenge-aware ideas.
- `ConfigurableAiContentIdeaProvider` is a remote-provider skeleton that requires future user-provided settings before any real integration.
- `FallbackContentIdeaProvider` catches provider failure and returns local fallback ideas.
- `ContentIdeaPromptBuilder` builds a structured, safe prompt without secrets.
- `AiContentSettings` models local-only or AI-assisted modes without storing any key in source code.
- No API key, paid AI dependency, or external request is used in UN-0007.

Content ideas are labeled as suggestions that must be reviewed before posting. The app avoids guaranteed sales/profit claims, fake scarcity, and unsupported sensitive claims.

Future AI integration should:

- Avoid hardcoded API keys.
- Use secure configuration.
- Clearly label generated suggestions.
- Keep disclaimers around non-guaranteed recommendations.
- Allow deterministic fallback samples for offline/test mode.
- Never log user API keys.

## Local-First Direction

The app works without login. User setup data is persisted locally with Room before any cloud sync is considered.

Current local-first behavior:

- Completed setup can be saved locally.
- Saved profile can be restored on app startup.
- Dashboard can use saved business data.
- Dashboard can use saved financial entries for monthly revenue, expenses, estimated profit, margin, target progress, recent entries, and trend visuals.
- Dashboard can generate rule-based business diagnosis insights from local profile and financial records.
- Weekly plans can be generated, saved, restored, and progressed locally.
- Dashboard can show active weekly plan progress.
- Content ideas can be generated, saved, filtered, favorited, planned, completed, and deleted locally.
- Dashboard can show content planning progress.
- Settings/Profile can delete the saved local profile.
- No authentication or cloud sync is used.
