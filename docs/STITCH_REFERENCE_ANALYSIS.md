# Stitch UI Reference Analysis - UsahaNaik Redesign Project

## Executive Summary

This document analyzes the Stitch AI design reference (`stitch_usaha_naik_redesign_project.zip`) and maps it to the existing UsahaNaik Android application. The goal is to modernize the visual language while preserving all existing business logic.

---

## Stitch Reference Screens Overview

### 1. Welcome / Onboarding (`welcome_to_usahanaik/`)
- **Visual**: Mesh gradient background with grain texture, asymmetric bento-grid preview
- **Components**: Large logo, headline, CTA buttons (Get Started / Log In), language selector
- **Interaction**: Parallax effect on hover, animated entrance

### 2. Login (`login/`)
- **Visual**: Clean centered card, hairline borders, focus rings
- **Components**: Email/password fields with visibility toggle, SSO buttons (Google/Apple), language selector in footer
- **Interaction**: Focus states, ripple on buttons, password visibility toggle

### 3. Executive Dashboard (`executive_dashboard/`)
- **Visual**: Bento grid layout (12-column), health score circular progress, KPI cards, AI insights, recent activity, quick actions
- **Components**: 
  - Health Score circular card (82/100)
  - Financial summary grid (Revenue, Expenses, Profit, Weekly Plan)
  - AI Growth Recommendations
  - Recent Activity list
  - Quick Actions (4 buttons)
- **Navigation**: Fixed bottom nav (5 tabs), top app bar with search

### 4. Financial Management (`financial_management/`)
- **Visual**: Summary cards, bar chart, filter chips, horizontal category scroll, transaction list
- **Components**:
  - 3 Summary cards (Income, Expense, Net Profit)
  - Profit & Loss trend chart
  - Filter bar (Period, Category, Type, Tune)
  - Category breakdown horizontal scroll
  - Transaction list with icons, badges

### 5. Weekly Planning (`weekly_planning/`)
- **Visual**: Calendar strip, priority-grouped tasks (High/Medium/Low), sidebar with challenge/milestones
- **Components**:
  - Weekly focus header with progress bar
  - Calendar day strip
  - Task list grouped by priority with checkboxes
  - Weekly Challenge card
  - Milestones with progress bars
  - Insights/tips card
  - FAB for mobile

### 6. Content Planner (`content_planner/`)
- **Visual**: Tabbed interface (Calendar/Ideas/Campaigns), bento grid
- **Components**:
  - AI-Generated Ideas feed with platform badges
  - Performance summary card with sparkline
  - Mini calendar with upcoming posts
  - FAB for generating ideas

### 7. Business Report (`business_report/`)
- **Visual**: Executive summary, KPI grid, SVG line chart, collapsible sections
- **Components**:
  - Executive summary with visual
  - 4 KPI cards (CAC, LTV, Burn Rate, Growth Margin)
  - Revenue growth chart (SVG)
  - Collapsible department breakdowns (Financials, Marketing, Operations)
  - Fixed bottom action bar (Download PDF)

### 8. Profile & Settings (`profile_settings/`)
- **Visual**: Profile header card, grouped settings with dividers
- **Components**:
  - Profile card with avatar, verified badge
  - Business Operations group (Business Profile, Strategic Goals)
  - Security & Preferences (Account, Appearance toggle, Notifications, Language)
  - System group (Data Management, Help & Support)
  - Logout button, version info

### 9. Design System (`executive_precision/DESIGN.md`)
- **Colors**: Deep Navy primary (#000000), Blue Accent (#0058be), Critical Orange (#F97316), sophisticated neutral grays
- **Typography**: Inter font family, negative letter-spacing for headings, generous leading for body
- **Spacing**: 8px base grid, 16px md, 24px lg, 40px xl
- **Elevation**: Tonal layering + 1px hairline borders, minimal shadows
- **Shapes**: 8px standard, 12px large containers
- **Components**: Flat buttons, flat inputs with focus ring, minimalist cards

---

## Current Application Screens Mapping

| Existing Screen | Stitch Reference Screen | Migration Strategy |
|----------------|------------------------|-------------------|
| `WelcomeScreen` | `welcome_to_usahanaik` | **Complete redesign**: Mesh gradient background, asymmetric bento preview, refined CTAs, language selector in footer |
| `LoginScreen` | `login` | **Complete redesign**: Centered card, hairline borders, focus rings, SSO buttons, forgot password, language selector in footer |
| `RegisterScreen` | (part of login flow) | **Redesign**: Match login visual language, same card style |
| `CategorySelectionScreen` | (onboarding) | **Redesign**: Integrate into onboarding flow, card-based selection |
| `BusinessSetupScreen` | (onboarding) | **Redesign**: Step-by-step with progress, card sections matching Stitch aesthetic |
| `DashboardScreen` | `executive_dashboard` | **Complete redesign**: Bento grid layout, health score circular card, KPI grid, AI insights, quick actions grid |
| `WeeklyPlanScreen` | `weekly_planning` | **Complete redesign**: Calendar strip, priority-grouped tasks, sidebar with challenge/milestones |
| `ContentIdeasScreen` | `content_planner` | **Complete redesign**: Tabbed interface, AI ideas feed, performance summary, mini calendar |
| `BusinessReportScreen` | `business_report` | **Complete redesign**: Executive summary, KPI cards, SVG chart, collapsible sections, fixed bottom action bar |
| `SettingsScreen` | `profile_settings` | **Complete redesign**: Profile header card, grouped settings with dividers, toggle switches |
| `WeeklyRetrospectiveScreen` | (part of dashboard/report) | **Redesign**: Match visual language, card-based layout |
| `AuthScreens.kt` components | `login` | **Complete redesign**: AuthScreenFrame, AuthTextField, AuthSwitchCard |

---

## Design System Migration Plan

### Colors - Current → Stitch
| Current | Stitch Target |
|---------|--------------|
| `CoralPrimary` (#F36F45) | `Primary` (#000000) / `Secondary` (#0058be) |
| `CoralSoft` | `SecondaryFixed` (#d8e2ff) |
| `CreamBackground` (#FFFFAF4) | `Background` (#f7f9fb) / `Surface` (#f7f9fb) |
| `CanvasSoft` | `SurfaceContainerLow` (#f2f4f6) |
| `SurfaceWarm` | `SurfaceContainerLowest` (#ffffff) |
| `Ink` / `InkStrong` | `OnSurface` (#191c1e) |
| `InkMuted` | `OnSurfaceVariant` (#45464d) |
| `BorderSubtle` | `OutlineVariant` (#c6c6cd) |
| `GreenPositive` | Keep for positive states (matches Blue Accent usage) |
| `CoralPrimary` for errors | `Error` (#ba1a1a) |

### Typography - Current → Stitch
| Current | Stitch Target |
|---------|--------------|
| SansSerif (default) | **Inter** (exclusive) |
| displayLarge 57sp | display-lg 32sp/40sp (-0.04em) |
| headlineLarge 32sp | headline-md 20sp/28sp (-0.02em) |
| titleLarge 22sp | body-lg 16sp/26sp |
| bodyLarge 16sp | body-md 14sp/22sp |
| labelLarge 14sp | label-sm 12sp/16sp (0.05em tracking) |

### Spacing - Current → Stitch
| Current | Stitch Target |
|---------|--------------|
| xxs 2dp | xs 4dp |
| xs 4dp | sm 8dp |
| sm 8dp | md 16dp (base) |
| md 12dp | lg 24dp |
| lg 16dp | xl 40dp |
| xl 24dp | - |
| xxl 32dp | - |
| xxxl 48dp | - |

**New**: Add container-margin 20dp, gutter 16dp

### Radius - Current → Stitch
| Current | Stitch Target |
|---------|--------------|
| xs 4dp | DEFAULT 4dp (0.25rem) |
| sm 8dp | lg 8dp (0.5rem) |
| md 12dp | xl 12dp (0.75rem) |
| lg 16dp | - |
| xl 20dp | - |
| full 999dp | full 9999dp |

### Elevation - Current → Stitch
| Current | Stitch Target |
|---------|--------------|
| Card: 1dp default | **Remove elevation from cards**, use hairline border (1px #E2E8F0) |
| Buttons: 2dp default | Flat buttons, no shadow |
| FAB: - | 40px touch targets with subtle shadow for modals only |

---

## Component-Level Migration

### 1. Cards
- **Current**: `UsahaNaikCard` with 16dp radius, 1dp elevation, BorderSubtle border
- **Stitch**: 12dp radius (xl), hairline border (#E2E8F0), NO shadow, 24dp internal padding

### 2. Buttons
- **Current**: `PrimaryActionButton` - CoralPrimary bg, 12dp radius, 2dp elevation
- **Stitch Primary**: Black bg (#000000), white text, 8dp radius, NO shadow, 48dp height
- **Stitch Secondary**: White bg, 1px border (#E2E8F0), Navy text, 8dp radius
- **Stretch/Full width**: Standard for primary actions

### 3. Inputs
- **Current**: `OutlinedTextField` with Material3 styling
- **Stitch**: Flat with 1px #E2E8F0 border, focus = Blue Accent border + 2px rgba(59,130,246,0.1) glow

### 4. Bottom Navigation
- **Current**: Material3 NavigationBar with tonalElevation 4dp
- **Stitch**: Fixed bottom, 56dp height, NO elevation, hairline top border, active = filled icon + primary color

### 5. Top App Bar
- **Current**: Standard Material3
- **Stitch**: Sticky, 56dp, hairline bottom border, search integrated, avatar + notification icons

### 6. Charts
- **Current**: Custom Canvas `TrendLineChart`
- **Stitch**: 2px stroke, no fill or 5% opacity gradient, sparklines for lists, data points on touch-hold

### 7. Chips/Badges
- **Current**: `PillBadge` with various colors
- **Stitch**: Soft tints (10% opacity of status color) + full saturation text, 2px radius or pill

---

## Migration Priority (Commits)

1. **Commit 1**: Audit complete, create analysis doc ✓
2. **Commit 2**: Design system foundation (colors, typography, spacing, radius, shapes)
3. **Commit 3**: Authentication redesign (Welcome, Login, Register)
4. **Commit 4**: Onboarding redesign (Category Selection, Business Setup)
5. **Commit 5**: Executive Dashboard redesign (most critical)
6. **Commit 6**: Financial Management redesign
7. **Commit 7**: Weekly Planner redesign
8. **Commit 8**: Content Planner redesign
9. **Commit 9**: Business Report redesign
10. **Commit 10**: Profile & Settings redesign
11. **Commit 11**: Navigation system (bottom nav, top app bar, FAB)
12. **Commit 12**: Reusable component library (cards, buttons, inputs, dialogs, sheets)
13. **Commit 13**: Micro-interactions (animations, ripple, transitions)
14. **Commit 14**: Accessibility (contrast, scaling, touch targets)
15. **Commit 15**: Runtime stabilization
16. **Commit 16**: Manual QA & emulator testing
17. **Commits 17-20**: Polish, bug fixes, final validation

---

## Critical Preservation Notes

### Must Preserve (Business Logic)
- All ViewModels and their state management
- Room database entities and DAOs
- Repository patterns
- StateFlow/LiveData streams
- Localization (Indonesian/English)
- Reminder/notification system
- Financial calculations
- Weekly plan generation
- Content idea generation (local + AI)
- Business report generation
- Navigation graph and routes

### Can Modify (UI Only)
- All Compose screen composables
- Theme (Color, Typography, Spacing, Shapes)
- Component library
- Screen layouts and visual hierarchy
- Animations and transitions
- Navigation bar styling

---

## Risk Assessment

| Risk | Impact | Mitigation |
|------|--------|------------|
| Theme breakage affecting all screens | High | Incremental migration, test each screen |
| Typography changes breaking layouts | Medium | Use relative sizing, test on multiple densities |
| Color scheme affecting accessibility | High | Verify contrast ratios after migration |
| Custom components (charts) needing rewrite | Medium | Preserve logic, only restyle |
| Bottom nav behavior changes | Medium | Keep same routes, only visual changes |

---

## Next Steps

1. Create new design system tokens matching Stitch spec
2. Build base components (Button, Input, Card, Badge, Chip)
3. Migrate authentication screens first (lowest risk)
4. Progress to dashboard (highest impact)
5. Continue through remaining screens
6. Final QA on emulator