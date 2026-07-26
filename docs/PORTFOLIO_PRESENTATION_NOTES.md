# Portfolio Presentation Notes

## Product Summary

UsahaNaik is a local-first Android business growth planner for Indonesian UMKM owners. It combines business setup, simple finance tracking, rule-based diagnosis, weekly plans, content planning, local calendar scheduling, retrospectives, reports, reminders, demo data, bilingual copy, and auth-ready entry screens.

## Engineering Highlights

- Kotlin Android app using Jetpack Compose and Material Design 3.
- Navigation Compose with auth/setup routes separated from main bottom tabs.
- Room database with local persistence for profile, finance, weekly plans, content, calendar, progress, retrospective, reports, and reminders.
- ViewModel-based state management for major workflows.
- Deterministic business diagnosis, weekly plan, retrospective, report, and content generation logic.
- Optional AI provider architecture with local deterministic fallback and no hardcoded keys.
- WorkManager-based reminder scheduling architecture with in-app fallback.
- Bilingual Indonesian/English copy provider and persisted language preference.
- Demo data seeding for portfolio walkthroughs.

## UI/UX Story

The finalization sprint improves safe-area spacing, bottom navigation comfort, visual density, copy clarity, workflow labels, and validation feedback. Dashboard now acts as the command center, while Plan, Ideas, Report, and Profile each keep a clearer product purpose.

## Demo Checklist

- Show the app launches cleanly.
- Show local mode entry from auth-ready screens.
- Load demo data.
- Demonstrate Dashboard metrics and source labels.
- Add financial entries.
- Complete a weekly task.
- Generate/save/schedule content ideas.
- Generate/save retrospective.
- Open Business Report and export-ready summary.
- Create a reminder.
- Switch language and restart if emulator time allows.

## Limitations To Say Out Loud

- Authentication screens are placeholders.
- Data is local only.
- No backend and no cloud sync.
- Content generation is deterministic/local in this version.
- Business recommendations are planning guidance, not guaranteed outcomes.
- Reports are not official accounting or tax documents.
- Notification delivery depends on Android runtime permission and OS scheduling.
