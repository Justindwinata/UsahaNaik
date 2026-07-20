# Auth-Ready Flow

## Purpose

UN-0013-R1 adds a professional auth-ready entry experience without implementing real authentication.

The goal is to make UsahaNaik feel closer to a production mobile product while keeping the app local-first and safe for portfolio use.

## Entry Routes

Routes:

- `welcome`
- `login`
- `register`
- `category_selection`
- `business_setup`
- `dashboard`

The welcome, login, and register screens are treated as onboarding routes, so the bottom navigation is hidden there.

## Welcome Flow

The welcome screen includes:

- App name and tagline.
- Indonesian/English language selector.
- Auth-ready login and register actions.
- Continue local mode action.
- Saved local profile resume card when available.
- Safe disclaimer that UsahaNaik does not guarantee profit and is not professional financial advice.

## Login Placeholder

The login screen includes:

- Email field.
- Password field.
- Login button placeholder.
- Continue local mode action.
- Register link.
- Language switch.
- Copy explaining that authentication is not enabled in this local demo version.

The password field is held only in Compose state while the screen is visible. It is not saved to Room, SharedPreferences, DataStore, or any backend.

## Register Placeholder

The register screen includes:

- Name field.
- Email field.
- Password field.
- Confirm password field.
- Register button placeholder.
- Continue local mode action.
- Login link.
- Language switch.
- Copy explaining that authentication is not enabled in this local demo version.

The password fields are held only in Compose state while the screen is visible. They are not persisted.

## Continue Local Mode

Continue local mode keeps the existing local-first app behavior:

- If a saved business profile exists, the user enters Dashboard.
- If no saved business profile exists, the user goes to category selection and business setup.

No account is required. No server request is made.

## Non-Goals

- No real authentication.
- No backend server.
- No cloud sync.
- No password storage.
- No fake account database.
- No production-ready account claims.

## Testing

Unit tests cover placeholder validation rules:

- Login requires email and password.
- Register requires name, email, password, and matching confirmation.
- Valid placeholder input passes validation.

Navigation and runtime UI behavior should be verified on an emulator/device when available.
