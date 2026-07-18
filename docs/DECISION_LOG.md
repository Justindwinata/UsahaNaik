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
