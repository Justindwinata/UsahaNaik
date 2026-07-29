# Visual And Localization Audit

## Project Detection

- Project name: UsahaNaik
- Project type: Android app
- Main stack: Kotlin, Jetpack Compose, Material Design 3, Navigation Compose, Room, WorkManager, JUnit
- Repository remote: `https://github.com/Justindwinata/UsahaNaik.git`
- Output directory for visual package: `dist/`
- Visual package name: `UsahaNaik_LinkedIn_Visual_Package.zip`

## Current Localization State

The project already includes a bilingual foundation:

- `AppLanguage` supports Indonesian and English.
- `AppCopyProvider` stores high-visibility static copy.
- `SharedPreferencesLanguagePreferenceRepository` persists selected language locally.
- `LanguageSelector` appears on auth entry and Profile surfaces.

Default language is Indonesian, which fits the target users: Indonesian UMKM owners.

## Localization Risks

- Some dynamic/generated business content remains deterministic and English-heavy because it is produced by rule-based generators.
- Some domain enum labels still use English model labels.
- Some Profile, finance, reminder, and generated-content helper text is still hardcoded in Compose or ViewModel messages.
- Full localization of every generated recommendation is intentionally deferred to avoid destabilizing business logic.

## Indonesian Support Plan

- Improve high-visibility Indonesian copy keys for visual/package messaging and local-first wording.
- Keep English available for portfolio review.
- Document that generated dynamic content may remain partially English/local deterministic.

## UI/UX Polish Needs

Recent finalization work already improved:

- safe-area spacing
- bottom navigation comfort
- shared card density
- dashboard state labels
- financial validation
- reminder time validation
- route continuity

Remaining deploy-readiness polish should stay low-risk:

- tighten high-visibility copy
- add screenshot workflow documentation
- create portfolio mockups and LinkedIn campaign visuals
- generate a visual-only package ZIP

## Screenshot Strategy

Runtime screenshots require an attached Android emulator/device. If no emulator/device is available during this task, runtime screenshots must not be claimed.

Fallback strategy:

- create a manual screenshot workflow under `docs/evidence/screenshots/final/`
- create clearly labeled mockup visuals based on actual project scope/source
- document that mockups are not runtime screenshots

## Visual Package Needs

Required output folders:

- `docs/evidence/screenshots/final/`
- `docs/visuals/mockups/`
- `docs/visuals/linkedin_campaign/`

Required package:

- `dist/UsahaNaik_LinkedIn_Visual_Package.zip`

The package must contain only visual/campaign assets and package documentation, not source code or build output.

## Product Truth Rules

Visuals and copy must not claim:

- real backend authentication
- cloud sync
- paid or live AI generation
- guaranteed profit or sales growth
- professional financial advice
- official accounting/tax reporting
- production deployment

Allowed positioning:

- local-first Android portfolio app
- rule-based business guidance
- deterministic/local content planning
- AI-ready architecture
- bilingual Indonesian/English app shell
- notification-ready local reminders
