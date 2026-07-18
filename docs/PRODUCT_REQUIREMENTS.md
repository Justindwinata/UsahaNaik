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
5. User opens a dashboard preview personalized from the draft state.
6. User reviews health score, finance summary, tasks, milestones, and ideas.
7. User follows weekly plan tasks.
8. User reviews AI-assisted content idea previews.
9. User updates profile/settings in future milestones.

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

## Limitations

- No authentication.
- No cloud database.
- No payment system.
- No Room persistence yet.
- Setup draft is not permanently saved yet.
- No notification system yet.
- No real AI integration yet.
- No full business diagnosis engine yet.
- No weekly plan generation from real diagnosis yet.
- No professional financial advice.
- No guaranteed profit increase.
