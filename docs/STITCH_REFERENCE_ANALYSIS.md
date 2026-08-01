# Stitch Reference Analysis - UsahaNaik UI Modernization

**Date**: August 2, 2026  
**Reference**: `UIReference/stitch_usaha_naik_redesign_project.zip`  
**Project**: UsahaNaik - AI-Powered Business Intelligence Suite

---

## Executive Summary

The Stitch reference provides a complete visual and UX overhaul of UsahaNaik using the "Executive Precision" design system. This system emphasizes clarity, authority, and high-velocity workflows through ultra-clean density, micro-precision grid alignment, and executive polish.

**Key Design Philosophy**: "The Informed Assistant"—a tool that feels quiet when idle but authoritative when providing insights.

The reference contains **10 fully designed screens** with HTML mockups, design tokens (color palette, typography, spacing), and comprehensive interaction patterns.

---

## Design System Reference (Executive Precision)

### Color Palette

**Primary Brand**
- **Primary**: `#000000` (Deep Navy/Black) - Typography, navigation, brand moments
- **On Primary**: `#FFFFFF` (Pure White)
- **Primary Container**: `#131B2E`
- **On Primary Container**: `#7C839B`

**Secondary (Growth Indicator)**
- **Secondary**: `#0058BE` (Blue Accent) - Progress, CTAs, active states
- **On Secondary**: `#FFFFFF`
- **Secondary Container**: `#2170E4`
- **On Secondary Container**: `#FEFCFF`

**Tertiary (Urgent/Critical)**
- **Tertiary**: `#000000`
- **On Tertiary**: `#FFFFFF`
- **Tertiary Container**: `#341100`
- **On Tertiary Container**: `#D95F00` (Critical Orange) - Urgent alerts, negative growth

**Neutral System (Hierarchy)**
- **Surface**: `#F7F9FB` (Cream Background)
- **On Surface**: `#191C1E` (Deep Ink)
- **On Surface Variant**: `#45464D` (Muted Ink)
- **Surface Container Lowest**: `#FFFFFF` (Cards)
- **Surface Container Low**: `#F2F4F6`
- **Surface Container**: `#ECEEF0`
- **Surface Container High**: `#E6E8EA`
- **Surface Container Highest**: `#E0E3E5`

**Error/Status**
- **Error**: `#BA1A1A` (Rose/Red)
- **On Error**: `#FFFFFF`
- **Error Container**: `#FFDAD6`

**Outlines**
- **Outline**: `#76777D` (Strong border/text)
- **Outline Variant**: `#C6C6CD` (Subtle hairline borders)

### Typography System

**Font Family**: Inter (exclusive, systematic, neutral, highly legible)

| Style | Size | Weight | Line Height | Letter Spacing |
|-------|------|--------|-------------|-----------------|
| **Display LG** | 32px | 700 | 40px | -0.04em |
| **Display LG Mobile** | 24px | 700 | 32px | -0.03em |
| **Headline MD** | 20px | 600 | 28px | -0.02em |
| **Body LG** | 16px | 400 | 26px | 0em |
| **Body MD** | 14px | 400 | 22px | 0em |
| **Label SM** | 12px | 600 | 16px | +0.05em |

**Typography Hierarchy**:
- Headings use tight negative letter-spacing (-0.02em to -0.04em) for "compact" authoritative look
- Body text uses generous leading (150%+) for readability during long data-review sessions
- Labels use uppercase with increased tracking to distinguish from actionable content
- Numerical data should use tabular lining figures for column alignment

### Layout & Spacing

**8px Grid System**:
- Base unit: 8px
- **xs**: 4px
- **sm**: 8px
- **md**: 16px
- **lg**: 24px
- **xl**: 40px
- **container-margin**: 20px
- **gutter**: 16px

**Padding Strategy**:
- Containers use `lg` (24px) or `xl` (40px) padding for premium gallery feel
- Vertical rhythm: `md` (16px) between related elements, `xl` (40px) between major sections

**Grid Model**:
- 12-column fluid grid for desktop/tablet
- 4-column fluid grid for mobile

### Elevation & Depth

**Surface Tiers**:
- Light mode: Base `#F8FAFC`, cards sit on `#FFFFFF`
- Dark mode: Base `#0F172A`, cards on `#1E293B`

**Borders**:
- Every card and interactive element: 1px border (`#E2E8F0` light / `rgba(255,255,255,0.1)` dark)
- "Hairline" aesthetic defines premium look
- On focus: Border changes to Blue Accent (`#3B82F6`) with 2px outer glow at 10% opacity

**Shadows**:
- Single "Ambient Shadow" for floating modals: `0px 4px 20px rgba(0, 0, 0, 0.05)`
- Avoid shadows on standard cards (maintain flat, architectural feel)

### Shape Language

| Component | Radius |
|-----------|--------|
| Standard (buttons, inputs, small cards) | 8px (0.5rem) |
| Large containers, dashboard widgets | 12px (0.75rem) |
| Interactive states (hover/focus) | Border remains 1px, color shifts to Blue Accent |

### Component Specs

**Buttons**
- **Primary**: Deep Navy background, Pure White text, no gradient, high-contrast
- **Secondary**: White background, 1px border, Navy text
- **Icon Buttons**: 20px icons centered in 40px bounding boxes (touch-friendly)

**Input Fields**
- Flat styling with 1px `#E2E8F0` border
- Focus: Blue Accent border with 2px outer glow
- Label: `label-sm` uppercase with increased tracking

**Dashboard Cards**
- Minimalist containers with 24px padding
- Header: `label-sm` for category, `headline-md` for primary metric
- Accent indicator: Small colored dot (12px circle)

**Charts & Data Visualization**
- Line Charts: 2px stroke, no area fill or subtle 5% opacity gradient
- Sparklines: 7-day trends, monochromatic (Blue) unless critical (Orange)
- Data points: Hidden by default, appear on touch-and-hold with vertical hairline cursor

**Chips/Badges**
- Small 2px radius or pill-shaped (999px)
- "Soft Tints": 10% opacity version of status color with full-saturation text
- Example: 10% Blue soft background with full Blue text for "In Progress"

---

## Screen-by-Screen Reference Mapping

### 1. Welcome Screen (`welcome_to_usahanaik`)

**Current App Status**: `WelcomeScreen` exists in `PlaceholderScreens.kt` (lines 175-286)

**Stitch Reference Features**:
- Minimalist header with logo + brand name
- Headline: "Elevate Your Business Growth."
- Subheadline: "The AI-powered assistant for modern entrepreneurs. Precision metrics and automated insights to scale your vision."
- Hero visual (asymmetric bento grid placeholder)
- Mesh gradient background (subtle radial gradients)
- CTA buttons: "Get Started" (primary) + "Log In" (secondary)
- Language toggle (EN/ID) in footer
- Footer links: Privacy Policy, Terms of Service
- Micro-interactions: Subtle parallax on hero containers, fade-in animations

**Migration Strategy**:
- Keep existing flow but apply Stitch styling
- Upgrade mesh gradient background
- Update button styles to match Stitch precision (hairline borders, no shadows)
- Add parallax micro-interaction
- Update typography to strict Inter + letter-spacing
- Ensure footer language selector matches Stitch design

---

### 2. Login Screen (`login`)

**Current App Status**: `LoginScreen` exists in `AuthScreens.kt` (lines 57-118)

**Stitch Reference Features**:
- Header: Logo + "UsahaNaik" brand name
- Headline: "Welcome Back."
- Subheadline: "Access your business intelligence suite."
- Form section (white card):
  - Email input field (flat, hairline border, blue focus ring)
  - Password field with visibility toggle
  - Error message display
  - Primary "Log In" button
  - "Continue Local Mode" button
- Divider: "Or continue with"
- SSO options: Google + Apple (2-column grid)
- Footer: "Don't have an account? Sign Up" + Language selector
- Input styling: Rounded corners (8px), 1px border, focus state with 2px glow

**Migration Strategy**:
- Current `LoginScreen` already uses `StitchInputField` and `StitchPasswordField`—good foundation
- Update colors to match Stitch exactly (verify `Secondary` = `#0058BE`)
- Ensure spacing follows 8px grid (currently uses `AppSpacing` which is good)
- Add SSO buttons (Google/Apple) matching Stitch spec
- Update form card styling to match precise Stitch border + radius
- Add proper focus animations (2px glow at 10% opacity)
- Verify button styles (primary: `#000000` bg + `#FFFFFF` text)

---

### 3. Register Screen (`login` - part of login screen group)

**Current App Status**: `RegisterScreen` exists in `AuthScreens.kt` (lines 120-238)

**Stitch Reference Features**:
- Similar layout to Login screen
- Headline: "Create Your Account."
- Subheadline: "Join thousands of entrepreneurs scaling with precision."
- Form fields:
  - Name/Owner Name input
  - Email input
  - Password input
  - Confirm Password input
  - Error message display
  - Primary "Register" button
  - "Continue Local Mode" button
- Same footer structure as Login

**Migration Strategy**:
- Already follows similar pattern to Login screen
- Apply same color/spacing/border refinements
- Ensure field labels use `label-sm` uppercase styling
- Add proper validation feedback (already done via `AuthEntryValidator`)
- Maintain consistent button styling

---

### 4. Welcome to UsahaNaik (Onboarding Flow)

**Stitch Covers**:
- Welcome screen ✓
- Login screen ✓
- Register screen ✓
- Category Selection (not explicitly in Stitch HTML, but referenced)
- Business Setup (not explicitly in Stitch HTML, but referenced)

**Current App Status**: 
- `CategorySelectionScreen` (lines 288-349)
- `BusinessSetupScreen` (lines 351-480)

**Gap Analysis**: Stitch doesn't provide detailed mockups for category selection or business setup. These should follow the same design system principles:
- Use consistent card styling (hairline borders, 12px radius for large containers)
- Apply strict 8px grid spacing
- Follow Inter typography system
- Use accent colors for primary actions and success states

---

### 5. Executive Dashboard (`executive_dashboard`)

**Current App Status**: `DashboardScreen` exists in `PlaceholderScreens.kt` (lines 903-1174) with many helper composables

**Stitch Reference Features**:
- **TopAppBar**: Logo + "UsahaNaik" + notification icon
- **Summary Cards Section** (3-column grid on desktop, single on mobile):
  - Total Income ($42,950.00) with +12% trend indicator
  - Total Expense ($18,420.00) with +4% trend indicator
  - Net Profit ($24,530.00) with trending_up icon
  - Cards: Hairline border, 12px radius, `label-sm` category label, `headline-md` value, colored accent dot
- **Chart Section**: "Profit & Loss Trend"
  - Export Report button (primary style, file_download icon)
  - Line chart (2px stroke, no fill or subtle gradient)
  - Daily breakdown (Mon-Sun) with colored bars (blue income, orange expense)
- **Overall Design**: Maximum width container, 20px margins, 24px internal padding in cards

**Migration Strategy**:
- Current dashboard is much simpler than Stitch reference
- Refactor `DashboardScreen` to match Stitch layout hierarchy
- Implement 3-column KPI card grid (responsive to 1 column on mobile)
- Upgrade chart visualization (current `TrendLineChart` needs refinement)
- Add export functionality to reports
- Update card styling to exact Stitch spec (hairline borders, 12px radius)
- Implement accent color dots for KPI cards
- Ensure all spacing follows 8px grid (16px md between sections, 24px lg for main padding)

---

### 6. Financial Management (`financial_management`)

**Current App Status**: Financial features exist but likely in placeholder form

**Stitch Reference Features**:
- Similar to dashboard but financial-focused
- Summary Cards: Total Income, Total Expense, Net Profit
- Chart Section: Profit & Loss Trend
- Could include transaction list, category breakdown, cash flow visualization

**Migration Strategy**:
- Apply same card/chart styling as dashboard
- Implement transaction list with proper typography hierarchy
- Add category filter chips (Stitch spec: soft tints)
- Ensure consistent color usage for income (blue/green) vs expense (orange)

---

### 7. Weekly Planning (`weekly_planning`)

**Current App Status**: `WeeklyPlanScreen` exists in `PlaceholderScreens.kt` (lines 2049-2108)

**Stitch Reference Features**:
- **TopAppBar**: Logo + "UsahaNaik" + notification icon
- **Header Section**:
  - "Active Sprint" label (`label-sm`)
  - "Weekly Focus: Increase Customer Retention" headline
  - Progress percentage and label on the right
  - Full-width progress bar (blue fill, hairline border, 8px radius)
- **Calendar Strip**: Horizontal scrollable week view
  - Day cards: Day label, date number
  - Current day: Primary container background
  - Other days: Surface background, hover effect
- **Main Content Grid** (8 cols: task list, 4 cols: sidebar):
  - **Task List by Priority**:
    - HIGH PRIORITY header (red dot, uppercase label)
    - Task items with checkboxes
    - Task title, completion indicator, time remaining
    - Strikethrough on completion
  - **Sidebar**:
    - Weekly goals section
    - Progress indicators
    - Quick actions

**Migration Strategy**:
- Current `WeeklyPlanScreen` needs significant UI overhaul
- Implement Stitch calendar strip design
- Add priority-based task grouping with color-coded headers
- Update progress bar to match Stitch spec (hairline border, 8px radius)
- Implement responsive grid layout (8-4 desktop, single column mobile)
- Add checkbox animations on completion
- Ensure proper spacing (16px md between sections)

---

### 8. Content Planner (`content_planner`)

**Current App Status**: `ContentIdeasScreen` exists in `PlaceholderScreens.kt` (lines 2533-2609)

**Stitch Reference Features**:
- Similar structure to Weekly Planning
- Calendar view for content scheduling
- Content idea cards with:
  - Title, description, platform selector
  - Status badge (Soft tints: "Scheduled", "Draft", "Published")
  - Schedule date/time
  - Platform icons (Instagram, Twitter, LinkedIn, TikTok)
- Idea generation controls
- Saved ideas vs generated ideas sections

**Migration Strategy**:
- Apply Stitch card styling to idea cards
- Implement status badge system (soft tint colors)
- Update calendar to match weekly planning calendar design
- Add platform selector chips with proper styling
- Ensure consistent typography and spacing

---

### 9. Business Report (`business_report`)

**Current App Status**: `BusinessReportScreen` exists in `PlaceholderScreens.kt` (lines 3700-3730)

**Stitch Reference Features**:
- Report generation interface
- KPI grid (similar to dashboard)
- Analytics charts (line charts, bar charts)
- Filterable tables
- Export functionality
- Report snapshot history

**Migration Strategy**:
- Apply Stitch dashboard styling to report KPIs
- Implement consistent chart styling
- Add filter chips for report customization
- Update export button to Stitch spec
- Implement table styling with proper typography hierarchy

---

### 10. Profile & Settings (`profile_settings`)

**Current App Status**: `SettingsScreen` exists in `PlaceholderScreens.kt` (lines 3062-3299)

**Stitch Reference Features**:
- **TopAppBar**: Menu + "UsahaNaik" + notification icon
- **User Profile Card**:
  - Avatar image with hairline border + verified badge
  - Name, role badge ("Executive"), email
  - Edit button (icon)
- **Settings Sections** (card groups):
  - **Business Operations**:
    - Business Profile → Retail, Tech
    - Strategic Goals → chevron_right
  - **Security & Preferences**:
    - Account Settings
    - Theme Toggle (dark/light)
    - Language Selector
    - Notification Settings
    - Backup & Restore
  - **About & Legal**:
    - About
    - Privacy Policy
    - Terms of Service
    - Logout button
- Each section in a separate card container
- Section headers: `label-sm` uppercase
- List items with icon + title + action/value

**Migration Strategy**:
- Redesign settings to match Stitch layout
- Implement profile card with avatar + verified badge
- Create settings section card components
- Add theme toggle (prepare for dark mode support)
- Implement language selector dropdown
- Add logout functionality with proper styling

---

## Color Usage Guide

### Current App vs Stitch

| Component | Current | Stitch |
|-----------|---------|--------|
| Primary Button | Primary (#000000) | Primary (#000000) ✓ |
| Secondary Button | OutlineVariant border | Secondary (#0058BE) |
| Accent/Growth | Secondary (#0058BE) | Secondary (#0058BE) ✓ |
| Background | Background (#F7F9FB) | Background (#F7F9FB) ✓ |
| Card Surface | SurfaceContainerLowest (#FFFFFF) | SurfaceContainerLowest (#FFFFFF) ✓ |
| Borders | OutlineVariant (#C6C6CD) | Outline Variant (#C6C6CD) ✓ |
| Error/Alert | Error (#BA1A1A) | Error (#BA1A1A) ✓ |
| Urgent/Critical | (missing) | On Tertiary Container (#D95F00) |
| Input Focus | Secondary glow | Blue Accent glow (10% opacity) |

**Finding**: The current color system is already largely aligned with Stitch! Minor adjustments needed:
- Define explicit "Critical Orange" usage for urgent alerts
- Ensure all input focus states use proper 2px glow at 10% opacity
- Add support for On Tertiary Container (#D95F00) for negative indicators

---

## Typography Usage Guide

### Current App vs Stitch

The app currently uses Material3 typography defaults. Stitch uses strict Inter with specific letter-spacing.

**Changes Needed**:
- Verify all headings use negative letter-spacing
- Ensure labels use uppercase + increased tracking
- Body text should use Inter (currently SansSerif, which varies by platform)
- Implement tabular figures for financial data

---

## Spacing & Layout Adjustments

### Current Implementation
- `AppSpacing` is defined (good!)
- Some screens use literal `dp` values instead of spacing tokens
- Inconsistent container margins

### Stitch Alignment
- Strict 8px grid throughout
- Container margins: 20px
- Internal padding: 24px (lg) or 40px (xl)
- Section spacing: 16px (md) between related, 40px (xl) between major

**Action**: 
- Audit all screens for literal `dp` usage
- Replace with `AppSpacing` tokens
- Update `AppRadius` for consistency (12px for large containers, 8px for standard)

---

## Navigation & Information Architecture

### Current App Routes
```
- Welcome
- Login / Register
- Category Selection
- Business Setup
- Dashboard (main app entry)
- Weekly Plan
- Content Ideas
- Retrospective
- Business Report
- Settings
```

### Stitch Flow
The Stitch reference doesn't show explicit navigation structure, but based on the 10 screens, the flow is:
1. Welcome → Get Started
2. Login/Register flow
3. Category Selection → Business Setup
4. Dashboard (hub)
5. Secondary flows: Weekly Planning, Content Planner, Reports, Profile/Settings

**Finding**: Current navigation structure aligns well with Stitch. No major changes needed to AppRoutes.

---

## Components to Create/Refactor

### Existing Components (in `ui/components/`)
- `UsahaNaikCard` ✓ (already uses AppRadius.md, 1dp border)
- `PrimaryActionButton` ✓ (matches Stitch spec)
- `ProfessionalKpiCard` ✓ (accent dot indicator)
- `ScreenHeroHeader` ✓
- `StatusBadge` ✓ (soft tints)
- `TrendLineChart` (needs refinement for Stitch spec)

### Components to Create
- `KpiCardGrid` (responsive 3/1 column layout)
- `ProgressBar` (hairline border, 8px radius, blue fill)
- `CalendarStrip` (horizontal week view with selection)
- `TaskListSection` (priority-based grouping)
- `PlatformSelector` (chip grid with icons)
- `ReportTable` (styled table with typography hierarchy)
- `AvatarWithBadge` (profile image with verified badge)
- `SettingsSectionCard` (grouped settings container)
- `ProgressBar` (visual progress indicator)
- `InputFocusRing` (2px glow effect)

---

## Micro-interactions & Animations

### Stitch Reference Specs
- Fade-in animations on hero elements (duration: 700ms)
- Slide-in from top (offset: 16px, duration: 700ms)
- Hover effects on cards (subtle opacity/shadow change)
- Button press animation: `active:scale-[0.98]`
- Smooth transitions: 200ms default, 150ms for press
- Scrollbar styling: Thin, rounded, subtle color
- Parallax effect on hero containers (subtle mouse tracking)

### Implementation in Compose
- Use `.animateIn()` modifiers
- `.active:scale()` for button press
- `.transition(all duration-150)` patterns
- Custom parallax using `PointerInput` or scroll state

---

## Accessibility Considerations

### Stitch Approach
- High contrast: Navy/Black on White (21:1 ratio)
- Minimum touch target: 40px (for icon buttons, 20px icon in 40px box)
- Color not sole indicator: Use icons + status badges
- Font scaling: Inter at various sizes with proper line-height
- Focus states: Blue accent border (visible)

### Current App Gaps
- No explicit accessibility testing mentioned
- Need to verify contrast ratios
- Touch target sizes should be 48dp minimum
- Focus state visibility needs verification

**Action**: 
- Audit colors for WCAG AA compliance
- Ensure all interactive elements have 48dp bounding boxes
- Add visible focus states
- Test with screen readers

---

## Responsive Design Strategy

### Stitch Breakpoints
- **Mobile**: 4-column grid, single column layouts
- **Tablet**: 8-12 column grid, 2-column layouts possible
- **Desktop**: 12-column grid, 3-column layouts

### Current App
- Uses Compose's `BoxScope` and `.weight()` for responsive layouts
- Some hardcoded values for mobile vs desktop

**Action**:
- Define explicit breakpoints in design system
- Create responsive layout helpers (e.g., `ResponsiveKpiGrid`)
- Test all screens on tablet and phone sizes

---

## Dark Mode Support

### Current Status
- Theme has `darkTheme` parameter but always uses `LightColorScheme`
- No dark color scheme defined

### Stitch Reference
- Light mode focused (reference doesn't show dark mode)
- But design system mentions: Dark mode surfaces #0F172A → #1E293B

**Future Enhancement**:
- Implement dark color scheme
- Toggle in Settings
- Respect system preference (with manual override)

---

## Summary of Migration Tasks

| Priority | Task | Component(s) | Est. Effort |
|----------|------|--------------|------------|
| **High** | Redesign Dashboard to match Stitch KPI layout | DashboardScreen, ProfessionalKpiCard | Large |
| **High** | Refine auth screens (Login, Register) with Stitch styling | AuthScreens.kt | Medium |
| **High** | Implement Weekly Planning layout with calendar strip | WeeklyPlanScreen | Large |
| **High** | Update all card styling to exact Stitch spec | UsahaNaikCard | Small |
| **Medium** | Add Stitch spacing/radius tokens consistently | All screens | Large |
| **Medium** | Implement Content Planner with Stitch styling | ContentIdeasScreen | Large |
| **Medium** | Redesign Settings screen with profile + sections | SettingsScreen | Medium |
| **Medium** | Create responsive KPI grid component | DashboardScreen | Small |
| **Low** | Add micro-interactions (parallax, animations) | All screens | Medium |
| **Low** | Refine chart styling to Stitch spec | TrendLineChart | Small |
| **Low** | Implement dark mode support | Theme.kt | Medium |

---

## Testing Checklist

- [ ] All colors match Stitch hex values exactly
- [ ] Typography uses Inter with correct letter-spacing
- [ ] Spacing follows strict 8px grid
- [ ] Border radius: 8px standard, 12px large containers
- [ ] All hairline borders: 1px with OutlineVariant color
- [ ] Button sizes: 48dp height, 20px icons in 40px boxes
- [ ] Card shadows: None (flat) except modals (0px 4px 20px rgba...)
- [ ] Focus states: 2px Blue Accent glow at 10% opacity
- [ ] Responsive layouts tested on mobile/tablet/desktop
- [ ] Accessibility contrast ratios verified (WCAG AA)
- [ ] Touch targets minimum 48dp
- [ ] Micro-interactions smooth and performant
- [ ] No crashes on device rotation
- [ ] Navigation flows work end-to-end
- [ ] Reminders still schedule correctly
- [ ] Local persistence works after UI changes
- [ ] All existing features function unchanged

---

## Implementation Order

1. **Phase 1 (Foundation)**: Update theme system, spacing tokens, color verification
2. **Phase 2 (Auth)**: Redesign Welcome, Login, Register screens
3. **Phase 3 (Core)**: Dashboard redesign (highest impact)
4. **Phase 4 (Secondary)**: Weekly Plan, Content Planner, Reports
5. **Phase 5 (Tertiary)**: Profile, Settings, Reminders
6. **Phase 6 (Polish)**: Micro-interactions, animations, accessibility
7. **Phase 7 (Validation)**: Runtime testing, QA, emulator verification

---

**Next Steps**: Begin Phase 1 with commit: `docs: analyze stitch ui reference`

