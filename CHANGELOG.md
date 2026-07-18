# Changelog

## 0.3.0 - UN-0003

- Added Room runtime, Room KTX, Room compiler, Room testing, and KSP configuration.
- Added `UsahaNaikDatabase`, `BusinessProfileEntity`, and `BusinessProfileDao`.
- Added `BusinessProfileRepository` and `LocalBusinessProfileRepository`.
- Added entity/domain mapping for setup drafts, enums, selected challenges, and metadata timestamps.
- Added save profile action from setup review.
- Added saved profile loading on app startup.
- Added saved profile resume card on welcome screen.
- Updated dashboard preview to prefer persisted profile data when available.
- Added Settings/Profile local data summary and delete confirmation.
- Added tests for mapping, repository save/read/update/delete, ViewModel save/load/delete, and dashboard mapping.

### Known Limitations

- Local persistence only.
- No cloud sync.
- No authentication.
- No real AI API integration yet.
- Content ideas are still local/sample-based.
- No full business diagnosis engine yet.
- No weekly plan generation from real diagnosis yet.
- No notification system.
- No guaranteed profit increase.
- Not professional financial advice.

## 0.2.0 - UN-0002

- Added `BusinessSetupDraft` domain model and setup enums for channel, stage, cost driver, stock issue, challenges, focus, and available time.
- Added deterministic setup validation with friendly field errors.
- Added Indonesian-style numeric parsing and derived calculations for profit margin, revenue target gap, and profit target gap.
- Added `BusinessSetupViewModel` and immutable `BusinessSetupUiState`.
- Added category-driven setup hints and recommended monthly focus metadata.
- Replaced setup placeholder with an interactive multi-section Compose form.
- Added challenge multi-select chips, selector chips, section completion indicators, and reset draft action.
- Added setup review section with financial summary and target gaps.
- Updated dashboard preview to use valid in-memory draft data when available.
- Added unit tests for validation, ViewModel state behavior, category hints, and dashboard draft mapping.

### Known Limitations

- No Room persistence yet.
- Setup draft is not permanently saved yet.
- No real AI API integration yet.
- Content ideas are still local/sample-based.
- No full business diagnosis engine yet.
- No weekly plan generation from real diagnosis yet.
- No notification system.
- No authentication.
- No cloud sync.
- No guaranteed profit increase.
- Not professional financial advice.

## 0.1.0 - UN-0001

- Initialized Android project foundation for UsahaNaik.
- Added baseline Gradle, Compose, Material 3, and Navigation Compose configuration.
- Added initial project documentation placeholders.
- Documented product requirements, architecture, roadmap, decision log, and UI reference analysis.
- Added Compose design system with warm UMKM dashboard colors, cards, badges, progress, metric, and chart components.
- Added Navigation Compose app shell with onboarding routes and bottom navigation.
- Added 10 business categories with metadata and setup form preview.
- Added dashboard preview, weekly plan preview, and AI-assisted content idea preview using local sample data.
- Added AI-ready `ContentIdeaProvider` interface and deterministic local provider.
- Added unit tests for category metadata and local content idea generation.

### Known Limitations

- No full business form persistence yet.
- No Room database yet.
- No real AI API integration yet.
- Content ideas are local preview samples.
- No real financial report persistence yet.
- No notification system yet.
- No authentication.
- No cloud sync.
- No guaranteed profit increase.
- Not professional financial advice.
