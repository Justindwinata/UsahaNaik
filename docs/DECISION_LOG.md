# Decision Log

## UN-0001 Decisions

### Android Native First

UsahaNaik is built as a native Android project using Kotlin and Jetpack Compose so the portfolio demonstrates modern Android engineering skills.

### Compose And Material Design 3

Compose and Material Design 3 are used for fast UI iteration, reusable components, and modern Android UI conventions.

### Dashboard-First Experience

The first polished experience is the growth dashboard because it best communicates the product value: structured business visibility for UMKM owners.

### Local Sample Data In UN-0001

UN-0001 uses local sample data so the app can build and run without login, cloud services, or paid AI dependencies.

### AI-Ready, Not AI-Dependent

The app defines AI content generation contracts but ships only a deterministic local provider in UN-0001. This avoids hardcoded API keys and keeps the project testable.

### No Guaranteed Financial Claims

The product language avoids guaranteed profit, guaranteed success, professional financial advice, or claims that AI recommendations are always correct.

### Room Later

Room database integration is deferred until the product forms and data model stabilize in later contracts.

### Compose-Drawn Chart First

UN-0001 uses a lightweight Compose `Canvas` trend chart instead of adding a charting library. This keeps dependency weight low and is enough for a portfolio preview.

### Deterministic Local AI Preview

The content ideas screen uses a deterministic provider. This keeps tests stable, prevents accidental paid API use, and creates a clean replacement boundary for later AI integration.

## UN-0002 Decisions

### In-Memory Draft Before Room

The setup flow stores draft data in `BusinessSetupViewModel` only. This gives the app a real interactive flow while keeping persistence deferred until the form model is stable enough for Room.

### String-Based Numeric Inputs

Financial values remain strings in `BusinessSetupDraft` so Compose text fields can accept Indonesian-style input naturally. Domain helpers parse values for validation and derived calculations.

### One Scrollable Setup Form

UN-0002 uses one scrollable multi-section form instead of a route-heavy wizard. This keeps navigation simple while still showing progress, section completion, inline errors, category hints, and review.

### Review Before Personalized Dashboard

The app shows a review section before dashboard creation so users can understand how their draft data will shape the preview. This avoids pretending the app has a full diagnosis engine in UN-0002.

### Dashboard Mapping Is Preview-Only

Draft values personalize the dashboard preview, but this is not a full business diagnosis engine. Deeper diagnosis and weekly plan generation are deferred to later milestones.

## UN-0003 Decisions

### Single Active Profile

UN-0003 stores one active business profile row. This keeps local persistence simple and fits the current product flow. Multi-business support can be added later without changing the user-facing promise of local-first setup.

### Room With KSP

Room uses KSP for annotation processing because the project already uses modern Kotlin and Compose. This keeps generated code integration explicit and buildable in Android Studio.

### Domain Model Kept Separate From Entity

The UI and dashboard use domain models. Room entities stay in the data layer, with mapper tests covering enum and challenge serialization.

### Local-Only Data Copy

Settings/Profile explicitly says the business profile is saved locally and not synced to cloud. This avoids implying authentication, backend storage, or remote AI processing.

### Delete Instead Of Account Reset

UN-0003 adds local profile deletion only. It does not add account management because there is no authentication or cloud account in this version.

## UN-0004 Decisions

### Simple Financial Records First

UN-0004 stores income and expense entries as simple local records. It does not introduce double-entry accounting, tax reporting, invoices, or payment features because the product goal is understandable UMKM dashboard visibility.

### Additive Room Migration

The database moves to version 2 with an additive `financial_entries` table. Existing `business_profiles` data remains intact, and destructive migration is avoided for this milestone.

### Dashboard Uses Entries With Baseline Fallback

Dashboard financial cards prefer persisted monthly financial entries when available. When no entries exist, the dashboard keeps using the saved setup baseline and prompts the user to start recording income and expenses.

### Estimated Profit Language

The app labels profit as estimated and calculates it transparently as income minus expenses. Product copy avoids tax, accounting, professional financial advice, or guaranteed profit claims.

### Finance UI Inside Dashboard

UN-0004 places the financial tracking form and recent activity inside the dashboard instead of adding a new bottom tab. This keeps navigation compact while the app still has only a small set of core flows.

## UN-0005 Decisions

### Rule-Based Diagnosis Before AI

UN-0005 adds deterministic diagnosis instead of AI-generated diagnosis. This keeps behavior transparent, testable, offline-friendly, and free from API keys or paid dependencies.

### Heuristic Health Score

The business health score is a heuristic 0-100 score built from financial clarity, profitability, goal progress, expense control, execution readiness, and category fit. It is useful for planning signals, not a scientific or professional financial rating.

### Insight Severity Labels

Insight cards use severity labels such as positive, info, warning, and critical. The UI also uses color, but the label is always visible so severity is not communicated by color alone.

### Category-Aware Actions Stay Practical

Priority actions are grounded in available app data and category metadata. The generator avoids recommendations that require unavailable data, such as tax calculations, inventory counts, or customer databases.

### Diagnosis Is Not Persisted Yet

Diagnosis is calculated in memory from Room-backed profile and financial records. Persisting diagnosis history is deferred until progress tracking and weekly plan generation are more mature.

## UN-0006 Decisions

### Deterministic Weekly Plans Before AI

UN-0006 generates weekly plans with local deterministic rules instead of AI. This keeps task generation explainable, testable, offline-friendly, and free from API keys or paid dependencies.

### One Active Weekly Plan

The app persists one active weekly plan. Regenerating replaces the active plan because the current product flow is focused on one UMKM owner following one weekly plan at a time.

### Task Completion Drives Milestone Progress

Task completion is persisted directly. Milestone progress is stored and recalculated from related task IDs where practical, keeping progress simple without adding a separate progress-history table yet.

### Weekly Plan Screen Owns Plan Management

Plan generation and regeneration live in the Weekly Plan tab. The dashboard shows a compact summary and CTA so the dashboard remains scannable.

### No Guaranteed Outcome Copy

Task expected outcomes use wording such as "may help" and "dapat membantu." Weekly plans are positioned as planning suggestions, not guaranteed sales, profit, or business success.
