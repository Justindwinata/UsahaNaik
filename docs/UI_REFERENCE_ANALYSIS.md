# UI Reference Analysis

Reference file: `UI_Reference.jpg`

## Observed Visual Direction

The reference shows a polished mobile dashboard with a soft, friendly, and highly visual card system. It uses off-white backgrounds, rounded pastel cards, compact metrics, a clean header, bottom navigation, progress indicators, small chart cards, and recommendation/task cards.

The layout feels approachable because each dashboard block is visually separated, short, and easy to scan. The hierarchy is clear: user context at the top, a primary progress card near the top, secondary metrics below, and quick actions or recommendation cards in the lower sections.

## Ideas Adopted For UsahaNaik

- Soft off-white app background.
- Warm coral/orange accent for primary actions and important growth signals.
- Soft green for positive progress and financial health.
- Soft blue/lavender cards for calm business insights.
- Rounded dashboard cards with lightweight borders and shadows.
- Header with business identity, current week, search, and notification affordances.
- Circular progress indicator translated into Business Growth Score.
- Simple line chart translated into revenue versus expense trend.
- Compact metric cards for monthly revenue, expenses, profit, and margin.
- Recommendation cards translated into today's business actions and content ideas.
- Bottom navigation translated into Dashboard, Plan, Ideas, and Profile.

## Ideas Adjusted

- Health labels are not reused. Fitness concepts are converted into business concepts.
- The UI does not claim guaranteed profit or guaranteed success.
- Cards use business planning language for Indonesian UMKM owners.
- Progress indicators are presented as planning and tracking signals, not as professional financial advice.
- Dashboard density is increased slightly so the app feels like a practical business planner instead of a lifestyle tracker.

## Implementation Notes

UN-0001 uses Jetpack Compose and Material Design 3 to build a dashboard-first product shell. Charts are intentionally Compose-drawn placeholders so the project stays lightweight and easy to build. The app uses sample local data only; future contracts will connect real form input, Room persistence, and AI integration.
