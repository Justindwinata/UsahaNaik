# UI Screenshot Guide for Portfolio Presentation

## Purpose

This guide provides instructions for capturing high-quality screenshots of UsahaNaik for portfolio, LinkedIn, and demo presentations.

## Prerequisites

- Android emulator or physical device running Android 8.0+ (API 26+)
- UsahaNaik app installed and running
- Demo data loaded (via Profile → Load Demo Data)

## Screenshot Specifications

### Resolution & Format
- **Target Resolution:** 1080x2340 (FHD+, typical for modern Android devices)
- **Format:** PNG (lossless compression)
- **Color Space:** sRGB
- **DPI:** 420-480 dpi

### Device Recommendations
- **Emulator:** Pixel 6 (1080x2340, 420 dpi)
- **Alternative:** Pixel 5 (1080x2340, 432 dpi)
- **Fallback:** Any Medium Phone with 1080x2340 resolution

## Screenshot Checklist

### 1. Welcome Screen ✓
**File:** `welcome_screen.png`
- Shows UsahaNaik logo
- Language selector visible (Indonesian selected)
- Auth-ready card with Login/Register buttons
- Local mode button prominent
- "Complete Setup" and "Dashboard" preview buttons
- Safety note card visible

### 2. Business Category Selection ✓
**File:** `category_selection.png`
- Shows all 10 business categories
- "Food & Beverage" selected
- Category cards with colored backgrounds
- Sample goal preview visible

### 3. Business Setup Form ✓
**File:** `business_setup.png`
- Setup progress indicator showing 3/5 sections complete
- Category hints card visible
- At least 2 form sections expanded
- Field validation examples visible
- "Review Setup" button at bottom

### 4. Dashboard - Overview ✓
**File:** `dashboard_overview.png`
- Business command center card at top
- KPI grid (Revenue, Expenses, Profit, Margin)
- Business health score with circular progress (75/100 or similar)
- Quick actions tiles (3 tiles visible)

### 5. Dashboard - Insights ✓
**File:** `dashboard_insights.png`
- Business diagnosis section
- Score breakdown with progress bars
- 2-3 business insight cards
- Priority actions section (at least 2 actions visible)

### 6. Dashboard - Financial Tracking ✓
**File:** `dashboard_financial.png`
- Revenue vs Expense trend chart
- Financial tracking form (income/expense entry)
- Recent financial activity (2-3 entries)
- Target progress indicators

### 7. Weekly Plan ✓
**File:** `weekly_plan.png`
- Weekly focus card (yellow background)
- Progress card showing task completion (e.g., 3/7)
- Task checklist with at least 3 tasks visible
- One task checked, two unchecked
- Challenge card visible at bottom

### 8. Weekly Plan - Milestones ✓
**File:** `weekly_plan_milestones.png`
- 3-5 milestone cards
- Progress percentages visible (e.g., 60%, 80%, 20%)
- Status badges (In Progress, Pending)
- Progress bars for each milestone

### 9. Content Planner - Generation ✓
**File:** `content_planner_generation.png`
- Generation controls card (lavender background)
- Platform selector (Instagram, TikTok, Facebook, etc.)
- Content goal chips
- "Generate Content Ideas" button
- Local generation badge visible

### 10. Content Planner - Ideas ✓
**File:** `content_planner_ideas.png`
- 2-3 generated content idea cards
- Hook, caption draft, CTA visible
- Platform and goal labels
- "Save Idea" or status badges
- Safety notes visible

### 11. Content Calendar ✓
**File:** `content_calendar.png`
- Scheduled content items (3-5 items)
- Status chips (Planned, Posted, Done, Skipped)
- Dates and time labels visible
- Platform badges
- Delete/Edit options visible

### 12. Weekly Retrospective ✓
**File:** `weekly_retrospective.png`
- Weekly progress snapshot card
- Task completion metrics (e.g., 5/7 tasks, 71%)
- Latest retrospective card with sections
- Next week suggestion visible
- Progress trend indicators

### 13. Business Report ✓
**File:** `business_report.png`
- Period selector (This Week, This Month, etc.)
- KPI overview grid (4 KPIs)
- Financial summary with bars
- Growth execution metrics
- Diagnosis summary with health score

### 14. Business Report - Export ✓
**File:** `business_report_export.png`
- Export-ready summary text preview
- "Save Local Snapshot" button
- Saved snapshots history (2-3 items)
- Period labels and timestamps

### 15. Profile - Settings ✓
**File:** `profile_settings.png`
- Language selector (Indonesian/English)
- Saved business profile card (Dapur Rasa Nusantara)
- Demo data controls
- Local reminders section
- AI provider settings

### 16. Profile - Reminders ✓
**File:** `profile_reminders.png`
- Notification permission status
- Reminder creation form
- Saved reminders list (3-5 reminders)
- Active/Paused status badges
- Frequency and time labels

### 17. Login Screen (Auth Placeholder) ✓
**File:** `auth_login.png`
- UsahaNaik logo
- Language selector
- Email and password fields
- "Local-first demo" badge
- "Continue Local Mode" button
- Switch to Register option

### 18. Register Screen (Auth Placeholder) ✓
**File:** `auth_register.png`
- Similar to login
- Name, email, password, confirm password fields
- Form validation example
- Local mode fallback visible

## Screenshot Capture Methods

### Method 1: Android Studio Emulator (Recommended)
```bash
# Start emulator
emulator -avd Pixel_6_API_35

# Capture screenshot via toolbar
# Camera icon → Save to docs/evidence/screenshots/final/
```

### Method 2: ADB Command
```bash
# Capture screenshot
adb shell screencap -p /sdcard/screenshot.png

# Pull to local
adb pull /sdcard/screenshot.png ./docs/evidence/screenshots/final/dashboard_overview.png

# Clean up device
adb shell rm /sdcard/screenshot.png
```

### Method 3: Physical Device
- Enable Developer Options
- Use "Screenshot" gesture (Power + Volume Down)
- Transfer via USB or Google Photos

## Screenshot Organization

```
docs/evidence/screenshots/final/
├── 01_welcome_screen.png
├── 02_category_selection.png
├── 03_business_setup.png
├── 04_dashboard_overview.png
├── 05_dashboard_insights.png
├── 06_dashboard_financial.png
├── 07_weekly_plan.png
├── 08_weekly_plan_milestones.png
├── 09_content_planner_generation.png
├── 10_content_planner_ideas.png
├── 11_content_calendar.png
├── 12_weekly_retrospective.png
├── 13_business_report.png
├── 14_business_report_export.png
├── 15_profile_settings.png
├── 16_profile_reminders.png
├── 17_auth_login.png
└── 18_auth_register.png
```

## Screenshot Quality Guidelines

### ✅ Do
- Use demo data (Dapur Rasa Nusantara) for realistic content
- Capture at native device resolution
- Ensure good lighting and contrast
- Show completed/in-progress states (not just empty states)
- Include at least 2-3 data items per list
- Show both metric values and visual progress indicators
- Capture clean UI without system notifications

### ❌ Don't
- Use empty states for portfolio screenshots
- Capture with low battery warnings
- Include personal data or real business information
- Use debug overlays or developer options in frame
- Capture during loading states
- Include system notifications or interruptions
- Show validation errors (unless demonstrating validation)

## Demo Data Preparation

Before capturing screenshots:

1. Open UsahaNaik app
2. Navigate to **Profile** tab
3. Tap **Load Demo Data**
4. Confirm data load
5. Wait for success message
6. Navigate to each screen to verify data loaded
7. Begin screenshot capture

## Post-Processing (Optional)

- **Resize:** Keep original resolution, or scale to 50% for web
- **Compress:** Use PNG optimization (e.g., pngquant, TinyPNG)
- **Frame:** Add device frame using [Mockuphone](https://mockuphone.com) or similar
- **Annotate:** Add callouts or arrows for specific feature highlights (for presentations only)

## LinkedIn Post Specifications

For LinkedIn carousel posts:
- **Format:** 1080x1080 (square) or 1200x1500 (portrait)
- **Crop:** Center-crop screenshots or add padding
- **Text Overlay:** Add feature name or benefit in top 20% of image
- **Branding:** Small UsahaNaik logo in corner
- **Sequence:** 4-6 slides showing app flow

## Portfolio Website Specifications

For portfolio website:
- **Hero Image:** Dashboard overview (center-cropped to 16:9 or 2:1)
- **Feature Grid:** 4-6 key screens (1:1 ratio, framed)
- **Lightbox Gallery:** All 18 screenshots available for detail view
- **Video Alternative:** Screen recording showing app flow (30-60 seconds)

## Verification Checklist

Before finalizing screenshots:

- [ ] All 18 required screenshots captured
- [ ] Demo data loaded and visible in all screens
- [ ] No personal/sensitive information visible
- [ ] Clean UI with no system overlays
- [ ] Consistent device/emulator used across all shots
- [ ] Files named according to convention
- [ ] Files saved in correct directory
- [ ] Image quality verified (no blur, compression artifacts)
- [ ] Text readable at 50% scale
- [ ] Status bar shows good signal/battery (if included)

## Known Limitations

- **No Runtime Screenshots Available:** If no emulator/device is attached, runtime screenshots cannot be captured
- **Mockup Alternative:** Use source-based mockups from `docs/visuals/mockups/` for presentation
- **Design Assets:** LinkedIn visuals in `docs/visuals/linkedin_campaign/` available without device
- **Placeholder Notice:** If screenshots cannot be captured, document this limitation clearly in portfolio materials

## Next Steps

After capturing screenshots:
1. Run verification checklist above
2. Commit screenshots to repository (if size permits) or store externally
3. Update `docs/PORTFOLIO_SHOWCASE.md` with screenshot references
4. Create device-framed versions for portfolio website
5. Prepare LinkedIn carousel using selected screenshots
6. Document screenshot capture date in progress report

---

**Document Version:** 1.0  
**Last Updated:** 2026-08-01  
**Prerequisite:** UN-UI-0001 Phase 13 completion
