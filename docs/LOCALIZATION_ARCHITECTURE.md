# Localization Architecture

## Purpose

UN-0013-R1 adds practical bilingual support for Indonesian and English while keeping the app stable during a large UI/UX revision.

The app defaults to Indonesian because the primary target users are Indonesian UMKM owners. English is available for portfolio review and bilingual demonstration.

## Supported Languages

- Indonesian (`id`) - default.
- English (`en`).

## Implementation

Core pieces:

- `AppLanguage` defines supported languages, language codes, display names, and default behavior.
- `AppStrings` defines high-visibility static UI copy used by the app shell and major screens.
- `AppCopyProvider` returns Indonesian or English copy.
- `LanguagePreferenceRepository` defines the local preference boundary.
- `SharedPreferencesLanguagePreferenceRepository` stores the selected language on device.
- `LanguageViewModel` exposes selected language state to Compose.
- `LocalAppLanguage` and `LocalAppStrings` provide the selected language and copy through composition.
- `LanguageSelector` is the reusable UI component for switching language.

## Persistence

The selected language is stored in local `SharedPreferences` under the app preferences file. This keeps language selection lightweight and avoids changing the Room schema.

The app does not sync language preference to cloud and does not require login.

## Scope For This Revision

The revision localizes high-visibility static copy:

- Welcome and auth-ready entry.
- Main navigation labels.
- Dashboard headings and primary cards.
- Setup, finance, plan, ideas, report, reminders, and profile headings.
- Major CTAs and empty-state copy.
- Safety disclaimers.

Generated or data-driven content can still include deterministic local text from existing business rules. Full generated-content translation is deferred until the product copy model stabilizes.

## Safety Copy

Both languages preserve the same product limitations:

- No guaranteed profit increase.
- Not professional financial advice.
- Local-first behavior.
- No real authentication yet.
- No real AI API integration yet.

## Testing

Unit tests cover:

- Indonesian as default language.
- Language code mapping.
- Indonesian and English copy lookup.
- ViewModel language switching.
- Repository persistence boundary through a fake repository.
