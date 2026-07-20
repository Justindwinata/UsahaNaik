# Product Requirements

## Product Problem

Many Indonesian UMKM owners run daily operations with limited structured records. They often know sales activity informally but may not consistently track revenue, expenses, profit margin, content planning, repeat orders, or milestone progress.

UsahaNaik helps owners organize business information and turn it into weekly planning, visible progress, and practical content ideas.

## Positioning

UsahaNaik helps UMKM owners plan, monitor, and improve business decisions through structured financial insights, weekly growth plans, milestones, and AI-assisted creative content ideas.

The app is not professional financial advice and does not guarantee profit increase, business success, or perfect AI recommendations.

## Target Users

- Warung and toko kelontong owners.
- Food and beverage businesses.
- Skincare and beauty sellers.
- Online shop and reseller owners.
- Fashion and thrift sellers.
- Laundry businesses.
- Coffee and beverage shops.
- Service businesses.
- Digital service and freelance businesses.
- Home-based businesses.

## Core Workflows

1. User opens the app and sees the value of structured UMKM planning.
2. User selects a business category.
3. User fills interactive business setup data.
4. User reviews the setup summary with derived margin and target gaps.
5. User saves the completed setup locally.
6. User opens a dashboard preview personalized from saved profile data.
7. User can close/reopen the app and resume the saved local profile.
8. User records simple income and expense entries locally.
9. User reviews dashboard financial metrics generated from saved entries.
10. User reviews rule-based business diagnosis insights and risk signals.
11. User follows 3-5 priority actions generated from category, challenges, goals, and financial records.
12. User generates a weekly growth plan from the latest local data.
13. User follows weekly plan tasks and marks tasks completed.
14. User reviews weekly challenge and milestones.
15. User sees weekly progress on the dashboard.
16. User opens Content Planner.
17. User selects platform, content goal, and optional content type.
18. User generates local deterministic content ideas.
19. User saves ideas, favorites ideas, marks ideas planned/done, or deletes ideas.
20. User reviews promotion campaign suggestions.
21. User sees content planning summary on the dashboard.
22. User schedules saved content ideas into a local content calendar.
23. User marks scheduled content planned, posted, skipped, or done.
24. User generates and saves a weekly progress snapshot.
25. User generates and saves a deterministic weekly retrospective.
26. User sees dashboard continuity cards for weekly progress, content execution, retrospective status, and trend history.
27. User opens Business Report.
28. User selects a report period.
29. User reviews KPI cards, simple visual summaries, financial summary, diagnosis summary, weekly execution, content performance, and retrospective highlights.
30. User reviews export-ready report text and can save a local report snapshot.
31. User sees a compact Business Report card on the dashboard.
32. User can load demo data from Settings/Profile for portfolio presentation.
33. User can clear demo data from Settings/Profile after confirmation.
34. User can delete local profile data from Settings/Profile.
35. User creates local reminders for finance tracking, weekly plan tasks, scheduled content, retrospectives, or report review.
36. User can enable, pause, edit, or delete local reminders.
37. User sees reminder summary and next reminder on the dashboard.
38. If notification permission is unavailable, user still has in-app reminder fallback.

## Business Categories

Initial categories include Food & Beverage, Warung / Toko Kelontong, Skincare & Beauty, Fashion / Thrift, Laundry, Online Shop / Reseller, Coffee / Beverage Shop, Service Business, Digital Service, and Other Business.

Each category carries display metadata, business focus area, setup hints, recommended monthly focus, and a sample recommended goal.

## Business Setup Workflow

UN-0002 implements an in-memory multi-section form:

- Business identity.
- Financial baseline.
- Product/service data.
- Business challenges.
- Monthly goals.
- Review summary.

Validation covers required business name, category, required financial values, product/service count, at least one challenge, target revenue, target profit, main focus, and available weekly time. Numeric fields reject negative values and accept common Indonesian-style currency input where practical.

Category-driven hints appear in the setup form so the selected business type influences guidance without hardcoding one category in the UI.

## Local Persistence

UN-0003 stores one active business profile locally using Room Database. Saved data includes business identity, financial baseline, product/service data, selected challenges, monthly goals, and metadata timestamps.

The profile is saved on-device only. UsahaNaik does not sync this data to cloud, does not require authentication, and does not send setup data to an external AI service in this version.

Users can delete the saved local profile from Settings/Profile. Deleting local profile removes the saved setup from this device.

## Financial Tracking

UN-0004 adds simple local financial records for income and expenses. Saved entry data includes type, title, amount, category, date, optional note, and metadata timestamps.

The app calculates dashboard metrics from saved entries where available:

- Total monthly income.
- Total monthly expenses.
- Estimated profit using `total income - total expenses`.
- Profit margin using `estimated profit / total income * 100`, or `0%` when income is zero.
- Target revenue and profit progress from saved business goals.
- Largest expense category.
- Recent financial activity.
- Revenue versus expense trend points.

This is intentionally simple financial tracking for owner visibility. It is not professional accounting software, tax reporting, or professional financial advice.

## Business Diagnosis Engine

UN-0005 adds deterministic rule-based diagnosis. Insights are generated from saved business profile data, selected challenges, business category, goals, available time, stock issue, cost driver, and local financial summary.

The business health score is heuristic and transparent. It uses these components:

- Financial clarity.
- Profitability.
- Goal progress.
- Expense control.
- Execution readiness.
- Category fit.

Diagnosis outputs include:

- Business health score and status label.
- Score breakdown.
- Business insight cards.
- Risk/attention signals.
- Dashboard insight summary counts.
- Category-aware priority actions with difficulty, estimated time, reason, and expected outcome wording.

Recommendations are planning suggestions, not guaranteed outcomes. UsahaNaik is not a replacement for professional financial advice.

## Weekly Growth Plan

UN-0006 adds a deterministic weekly growth plan generator. Plans are generated from saved profile data, financial summary, diagnosis result, selected challenges, business category, goals, stock issue, cost driver, and available weekly time.

Each weekly growth plan includes:

- Week title and generated date.
- Business category.
- Weekly focus and target.
- Priority reason.
- 5-7 practical tasks.
- One weekly challenge.
- 3-5 milestones.
- Progress summary.
- Limitations note.

Weekly plan tasks include title, description, category, estimated time, difficulty, status, reason, and non-guaranteed expected outcome wording.

The active weekly plan is saved locally with Room. Task completion persists, and milestone progress is derived from related task completion where practical.

Plans are rule-based suggestions for structured action. They do not guarantee revenue growth, sales growth, or business success.

## Dashboard Concept

The dashboard should show:

- Business summary.
- Business health score.
- Monthly revenue and profit overview.
- Expense breakdown.
- Product/service performance.
- Weekly goals.
- Milestones.
- Tasks and challenges.
- AI-assisted content ideas preview.
- Promotion and education idea sections.
- Financial report summary.
- Progress tracking.
- Recommendations and next actions.

## AI Content Idea Concept

The app will eventually support AI-assisted generation for captions, campaign ideas, educational content, promotion angles, weekly suggestions, and business recommendations.

For UN-0001, AI content ideas are local deterministic samples. No real API calls, paid dependency, external model, or API key is included.

For UN-0002, content ideas remain local deterministic samples. The setup draft can personalize the dashboard preview, but the app still does not call a real AI API.

For UN-0003, content ideas remain local deterministic samples. Room persistence only applies to the business setup/profile data.

For UN-0004, content ideas remain local deterministic samples. Room persistence now also stores simple financial entries, but no real AI API is used.

For UN-0005, content ideas remain local deterministic samples. The diagnosis engine is rule-based and does not call an AI API.

For UN-0006, content ideas remain local deterministic samples. Weekly plans are also deterministic and do not call an AI API.

For UN-0007, Content Planner becomes an interactive feature. It uses a deterministic local provider to generate 5-10 category-aware and challenge-aware content ideas with hook, caption draft, CTA, visual suggestion, posting note, and safety note. Generated ideas can be saved locally, favorited, marked planned, marked done, deleted, and filtered by status.

UN-0007 also adds optional AI provider architecture with settings model, prompt builder, configurable provider skeleton, and fallback wrapper. Remote AI generation is not implemented yet. The app remains useful offline and does not require paid AI access.

Content generation safety requirements:

- No hardcoded API keys.
- No committed secrets.
- Local deterministic generation must always work.
- Generated ideas are suggestions and should be reviewed before posting.
- Avoid guaranteed sales, guaranteed profit, fake scarcity, unsupported product claims, and sensitive medical/legal/financial claims.
- Skincare and beauty content must avoid cure or guaranteed result claims.
- Food and beverage content must avoid health claims unless independently supported.

## Progress Continuity

UN-0008 adds continuity features so users can monitor business execution over time.

Content calendar:

- Saved content ideas can be scheduled locally.
- Scheduled items include date, optional time label, posting note, platform, and status.
- Supported statuses are planned, posted, skipped, and done.
- This is an internal app calendar only. It does not use Android Calendar Provider, external calendar sync, calendar permissions, or notifications.

Weekly progress history:

- The app can generate a weekly progress snapshot from local weekly plan progress, financial summary, content calendar summary, saved ideas count, and diagnosis signals.
- Snapshots include task completion, milestone progress, income, expenses, estimated profit, margin, content execution counts, business health score, and warning/critical counts.
- One snapshot per week can be replaced locally.

Weekly retrospective:

- The app can generate a deterministic retrospective from the current snapshot and active weekly plan.
- Retrospective sections include what improved, what still needs attention, completed tasks, missed tasks, content execution summary, financial summary, and next-week suggestion.
- Copy uses careful wording such as "may indicate", "suggests", "consider", and "can help".
- Retrospectives are planning summaries, not professional advice or guaranteed outcomes.

## Business Report Dashboard

UN-0009 adds a Business Report dashboard that combines existing local app data into an owner-friendly report view.

Supported report periods:

- This week.
- This month.
- Last 30 days.
- All local data.

Report sections include:

- KPI overview for revenue, expenses, estimated profit, profit margin, business health, task completion, milestone progress, and content execution.
- Financial summary with revenue versus expense visual bars, expense breakdown, largest expense category, and limited-data guidance.
- Growth progress summary from the active weekly plan.
- Diagnosis summary with health score, warnings, critical count, top insights, and priority actions.
- Content performance summary from saved ideas and scheduled content status.
- Retrospective highlights from the latest saved weekly retrospective.
- Export-ready text report preview.

Generated reports are local planning summaries. They are not official accounting reports, tax documents, audited statements, or professional financial advice. UN-0009 does not implement PDF export.

## Demo Mode And Portfolio Readiness

UN-0010 adds local Demo Mode so the app can be presented quickly without manual data entry.

Demo Mode:

- Is accessed from Settings/Profile.
- Shows a confirmation before replacing local app data.
- Loads the sample Food & Beverage business `Dapur Rasa Nusantara`.
- Populates business profile, financial entries, weekly plan, content ideas, content schedules, progress snapshot, retrospective, and report snapshot.
- Can be cleared from Settings/Profile.

Demo data is sample local data for portfolio presentation. It does not represent real business results, guaranteed sales growth, guaranteed profit increase, or professional financial advice.

UN-0010 also improves shared empty/loading/error states, dashboard/report presentation, planning/content/retrospective workflows, navigation refresh behavior, and portfolio documentation.

Dashboard continuity:

- Dashboard shows weekly completion, content calendar execution, latest retrospective takeaway, and simple progress trend cards.

## Local Reminders And Notification-Ready Planning

UN-0011 adds optional local reminders so UMKM owners can remember important business routines without login or cloud sync.

Supported reminder types:

- Daily financial tracking.
- Weekly plan task review.
- Scheduled content preparation.
- Weekly retrospective.
- Business report review.

Reminder behavior:

- Reminders are saved locally in Room.
- Users can create, edit, enable, pause, and delete reminders from Profile.
- Dashboard shows active reminder count, paused count, next reminder, and notification permission state.
- Demo Mode includes sample reminders for Dapur Rasa Nusantara.
- If Android notification permission is unavailable or denied, reminders still appear in-app.

Notification-ready scope:

- UN-0011 adds notification permission state handling, notification channel setup, safe reminder messages, and scheduler abstraction.
- Exact OS-level alarm/work scheduling is deferred to a later milestone and requires emulator/device QA.
- The app does not claim cloud reminders, server reminders, or guaranteed habit/business outcomes.

## Limitations

- No authentication.
- No cloud database or cloud sync.
- No payment system.
- Local persistence only.
- Local reminders exist, but exact system notification scheduling is not implemented yet.
- Notification behavior requires device/emulator QA.
- No external calendar integration.
- No real AI integration yet.
- AI provider architecture is present, but remote AI generation is not implemented yet.
- Content generation remains local deterministic in the app build.
- Content ideas should be reviewed before posting.
- Diagnosis is deterministic and heuristic.
- Weekly plan is deterministic and heuristic.
- Progress history is deterministic.
- Weekly retrospective is deterministic and heuristic.
- No professional financial advice.
- Not professional accounting software.
- No guaranteed profit increase.
