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
10. User reviews health score, finance summary, tasks, milestones, and ideas.
11. User follows weekly plan tasks.
12. User reviews AI-assisted content idea previews.
13. User can delete local profile data from Settings/Profile.

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

## Limitations

- No authentication.
- No cloud database or cloud sync.
- No payment system.
- Local persistence only.
- No notification system yet.
- No real AI integration yet.
- No full business diagnosis engine yet.
- No weekly plan generation from real diagnosis yet.
- No professional financial advice.
- Not professional accounting software.
- No guaranteed profit increase.
