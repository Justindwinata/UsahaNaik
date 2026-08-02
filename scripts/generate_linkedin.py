"""Generate Usaha Naik LinkedIn campaign slides."""
import sys, os
sys.path.insert(0, os.path.dirname(__file__))
from campaign_style import *

OUT = os.path.join(os.path.dirname(__file__), "..", "docs", "visuals", "linkedin")

def slide_cover():
    img, d = new_canvas(NAVY)
    # subtle mesh accents
    d.ellipse([-150, -150, 500, 200], fill=(19,27,46))
    d.ellipse([850, 380, 1350, 750], fill=(33,112,228))
    top_accent(d, BLUE_SOFT)
    logo_mark(d, (48, 48), 52)
    f = font(15)
    d.text((112, 66), "USAHANAIK", font=f, fill=WHITE)
    badge(d, (48, 220), "ANDROID · AI-POWERED", bg=BLUE_SOFT, fg=WHITE, size=15)
    headline(d, (48, 300), "Elevate Your\nBusiness Growth.", 56, WHITE)
    body(d, (48, 500), "The AI-powered business intelligence suite for modern entrepreneurs. Precision metrics and automated insights to scale your vision.", 22, "#C7D2E8", max_width=800)
    footer(d, "UsahaNaik  ·  LinkedIn Launch Series  ·  Slide 01 / 14")
    save(img, f"{OUT}/01_cover.png")

def slide_problem():
    img, d = new_canvas()
    top_accent(d)
    badge(d, (48, 40), "THE PROBLEM", size=14)
    headline(d, (48, 90), "Great businesses fail\nfrom blind decisions.", 44)
    body(d, (48, 300), "Most SME owners run on gut feeling. Revenue is unclear. Costs are scattered. Weekly plans get lost in messaging apps.", 21, max_width=700)
    # panel list
    problems = [
        ("No financial clarity", "Income and expenses never tracked consistently."),
        ("No structured planning", "Weekly focus and tasks exist only in memory."),
        ("No content consistency", "Marketing happens whenever there's spare time."),
    ]
    y = 410
    for t, s in problems:
        panel(d, [48, y, 720, y+64], radius=14)
        d.ellipse([70, y+22, 86, y+38], fill=ORANGE)
        d.text((100, y+14), t, font=font(19), fill=INK)
        d.text((100, y+38), s, font=font(14, False), fill=INK_MUTED)
        y += 80
    footer(d, "UsahaNaik  ·  LinkedIn Launch Series  ·  Slide 02 / 14")
    save(img, f"{OUT}/02_problem.png")

def slide_solution():
    img, d = new_canvas()
    top_accent(d)
    badge(d, (48, 40), "THE SOLUTION", size=14)
    headline(d, (48, 90), "One local-first assistant\nfor every business decision.", 40)
    body(d, (48, 290), "UsahaNaik turns raw business data into clear priorities, weekly plans, and content that keeps your brand consistent.", 21, max_width=720)
    features = [
        ("Dashboard", "Executive KPI overview in one glance"),
        ("Financial", "Track income, expenses & profit margin"),
        ("Planner", "Weekly goals, tasks & milestones"),
        ("Content", "AI-assisted idea generation & scheduling"),
    ]
    # 2x2 grid of solution cards
    positions = [(48, 400), (620, 400), (48, 500), (620, 500)]
    for (t, s), (x, y) in zip(features, positions):
        panel(d, [x, y, x+520, y+88], radius=16)
        d.rounded_rectangle([x+16, y+22, x+44, y+50], radius=6, fill=BLUE_LIGHT)
        f = font(16)
        d.text((x+26, y+28), t[0], font=f, fill=BLUE)
        d.text((x+60, y+14), t, font=font(19), fill=INK)
        d.text((x+60, y+42), s, font=font(14, False), fill=INK_MUTED)
    footer(d, "UsahaNaik  ·  LinkedIn Launch Series  ·  Slide 03 / 14")
    save(img, f"{OUT}/03_solution.png")

def slide_features():
    img, d = new_canvas()
    top_accent(d)
    badge(d, (48, 40), "CORE FEATURES", size=14)
    headline(d, (48, 90), "Everything an SME needs,\nnothing they don't.", 40)
    cols = [
        ("Local-First", "All data stays on device. No cloud dependency, no lock-in."),
        ("Rule-Based AI", "Deterministic insights and recommendations you can trust."),
        ("Reminders", "Schedule business reminders that actually fire."),
        ("Reports", "Export-ready summaries for stakeholders."),
    ]
    x = 48
    for t, s in cols:
        panel(d, [x, 320, x+260, 560], radius=20)
        d.rounded_rectangle([x+20, 340, x+52, 372], radius=8, fill=BLUE)
        d.text((x+20, 395), t, font=font(20), fill=INK)
        body(d, (x+20, 430), s, 15, max_width=220, line_spacing=1.3)
        x += 276
    footer(d, "UsahaNaik  ·  LinkedIn Launch Series  ·  Slide 04 / 14")
    save(img, f"{OUT}/04_features.png")

def slide_dashboard():
    img, d = new_canvas()
    top_accent(d)
    badge(d, (48, 40), "EXECUTIVE DASHBOARD", size=14)
    headline(d, (48, 90), "Business health at a glance.", 40)
    body(d, (48, 260), "KPI cards, profit & loss trends, AI recommendations, and business signals on one screen.", 20, max_width=680)
    # mockup frame
    panel(d, [620, 180, 1160, 560], radius=24, fill=WHITE)
    d.rounded_rectangle([660, 220, 1120, 260], radius=12, fill=SURFACE)
    d.text((680, 230), "Total Income      Total Expense      Net Profit", font=font(15), fill=INK_MUTED)
    # bars
    d.rounded_rectangle([660, 290, 760, 480], radius=8, fill=BLUE)
    d.rounded_rectangle([790, 340, 890, 480], radius=8, fill=ORANGE)
    d.rounded_rectangle([920, 250, 1020, 480], radius=8, fill=BLUE_LIGHT)
    d.text((660, 500), "Profit & Loss Trend", font=font(15), fill=INK)
    d.text((48, 320), "•  Revenue vs Expense trend chart", font=font(17, False), fill=INK_MUTED)
    d.text((48, 360), "•  Business health score", font=font(17, False), fill=INK_MUTED)
    d.text((48, 400), "•  Weekly goal progress", font=font(17, False), fill=INK_MUTED)
    footer(d, "UsahaNaik  ·  LinkedIn Launch Series  ·  Slide 05 / 14")
    save(img, f"{OUT}/05_dashboard.png")

def slide_financial():
    img, d = new_canvas()
    top_accent(d)
    badge(d, (48, 40), "FINANCIAL MANAGEMENT", size=14)
    headline(d, (48, 90), "Know your numbers.", 44)
    body(d, (48, 280), "Record income and expenses with structured categories. Watch your profit margin in real time.", 20, max_width=680)
    # financial panel mockup
    panel(d, [620, 200, 1160, 560], radius=24)
    d.rounded_rectangle([660, 240, 1060, 300], radius=10, fill=SURFACE)
    d.text((680, 252), "+  Product Sales     Rp 1,000,000", font=font(16), fill=BLUE)
    d.rounded_rectangle([660, 320, 1060, 380], radius=10, fill=SURFACE)
    d.text((680, 332), "-  Raw Materials     Rp 250,000", font=font(16), fill=ORANGE)
    d.rounded_rectangle([660, 400, 1060, 460], radius=10, fill=SURFACE)
    d.text((680, 412), "+  New Customer     Rp 750,000", font=font(16), fill=BLUE)
    d.text((660, 500), "Net Profit: Rp 4,200,000  ·  Margin 38%", font=font(16), fill=NAVY)
    d.text((48, 320), "•  Income & expense tracking", font=font(17, False), fill=INK_MUTED)
    d.text((48, 360), "•  Category-based breakdown", font=font(17, False), fill=INK_MUTED)
    d.text((48, 400), "•  Revenue target progress", font=font(17, False), fill=INK_MUTED)
    footer(d, "UsahaNaik  ·  LinkedIn Launch Series  ·  Slide 06 / 14")
    save(img, f"{OUT}/06_financial.png")

def slide_planner():
    img, d = new_canvas()
    top_accent(d)
    badge(d, (48, 40), "WEEKLY PLANNER", size=14)
    headline(d, (48, 90), "Turn strategy into\nweekly action.", 40)
    body(d, (48, 290), "Weekly focus, tasks, milestones, and a retrospective loop that keeps your business moving forward.", 20, max_width=700)
    # progress mockup
    panel(d, [620, 200, 1160, 560], radius=24)
    d.text((660, 230), "Active Sprint", font=font(14), fill=BLUE)
    d.text((660, 260), "Increase Customer Retention", font=font(20), fill=NAVY)
    d.text((1000, 252), "75%", font=font(24), fill=BLUE)
    d.rounded_rectangle([660, 310, 1120, 330], radius=8, fill="#E2E8F0")
    d.rounded_rectangle([660, 310, 1005, 330], radius=8, fill=BLUE)
    tasks = ["High priority: Follow-up calls", "Medium: Product content", "Done: Invoice review"]
    y = 360
    for i, t in enumerate(tasks):
        done = i == 2
        d.rounded_rectangle([660, y, 1120, y+52], radius=10, fill=SURFACE)
        if done:
            d.rounded_rectangle([676, y+14, 694, y+32], radius=4, fill=BLUE)
            d.line([(704, y+20), (900, y+20)], fill=OUTLINE, width=1)
        else:
            d.rounded_rectangle([676, y+14, 694, y+32], radius=4, outline=OUTLINE, width=1)
        d.text((710, y+14), t, font=font(15, not done), fill=INK_MUTED if done else INK)
        y += 62
    d.text((48, 320), "•  Priority-based task grouping", font=font(17, False), fill=INK_MUTED)
    d.text((48, 360), "•  Goal progress tracking", font=font(17, False), fill=INK_MUTED)
    d.text((48, 400), "•  Weekly retrospective review", font=font(17, False), fill=INK_MUTED)
    footer(d, "UsahaNaik  ·  LinkedIn Launch Series  ·  Slide 07 / 14")
    save(img, f"{OUT}/07_planner.png")

def slide_content():
    img, d = new_canvas()
    top_accent(d)
    badge(d, (48, 40), "CONTENT PLANNER", size=14)
    headline(d, (48, 90), "Never run out of\ncontent again.", 44)
    body(d, (48, 290), "Generate ideas, save the best, schedule posts across platforms, and track what's published.", 20, max_width=700)
    panel(d, [620, 200, 1160, 560], radius=24)
    d.text((660, 225), "Generated Ideas", font=font(16), fill=NAVY)
    ideas = ["Post: Behind-the-scenes production", "Reel: 3 pricing mistakes to avoid", "Story: Customer success snippet"]
    y = 265
    for i, idea in enumerate(ideas):
        d.rounded_rectangle([660, y, 1120, y+80], radius=12, fill=SURFACE)
        d.rounded_rectangle([676, y+16, 694, y+34], radius=4, fill=BLUE)
        d.text((710, y+16), "Instagram", font=font(13), fill=BLUE)
        d.text((710, y+40), idea, font=font(15), fill=INK)
        y += 92
    d.text((48, 320), "•  Platform-specific idea generation", font=font(17, False), fill=INK_MUTED)
    d.text((48, 360), "•  Content calendar scheduling", font=font(17, False), fill=INK_MUTED)
    d.text((48, 400), "•  Status tracking (draft / scheduled / done)", font=font(17, False), fill=INK_MUTED)
    footer(d, "UsahaNaik  ·  LinkedIn Launch Series  ·  Slide 08 / 14")
    save(img, f"{OUT}/08_content.png")

def slide_reports():
    img, d = new_canvas()
    top_accent(d)
    badge(d, (48, 40), "REPORTS & ANALYTICS", size=14)
    headline(d, (48, 90), "Stakeholder-ready reports\nin one tap.", 40)
    body(d, (48, 290), "KPI overview, financial summaries, execution tracking, and export-ready report text.", 20, max_width=700)
    panel(d, [620, 200, 1160, 560], radius=24)
    d.text((660, 225), "KPI Overview", font=font(16), fill=NAVY)
    kpis = [("Revenue", "Rp 42.9M", BLUE), ("Expenses", "Rp 18.4M", ORANGE), ("Profit", "Rp 24.5M", NAVY)]
    x = 660
    for t, v, c in kpis:
        d.rounded_rectangle([x, 260, x+150, 360], radius=12, fill=SURFACE)
        d.text((x+12, 280), t, font=font(13), fill=INK_MUTED)
        d.text((x+12, 305), v, font=font(17), fill=c)
        x += 162
    d.text((660, 390), "Export Report", font=font(16), fill=WHITE)
    d.rounded_rectangle([660, 380, 1120, 430], radius=10, fill=NAVY)
    d.text((700, 398), "⬇  Download PDF Summary", font=font(15), fill=WHITE)
    d.text((48, 320), "•  Period selector (weekly / monthly)", font=font(17, False), fill=INK_MUTED)
    d.text((48, 360), "•  Expense breakdown charts", font=font(17, False), fill=INK_MUTED)
    d.text((48, 400), "•  Snapshot history & export", font=font(17, False), fill=INK_MUTED)
    footer(d, "UsahaNaik  ·  LinkedIn Launch Series  ·  Slide 09 / 14")
    save(img, f"{OUT}/09_reports.png")

def slide_ai():
    img, d = new_canvas(NAVY)
    d.ellipse([900, -100, 1400, 300], fill=(33,112,228))
    top_accent(d, BLUE_SOFT)
    badge(d, (48, 40), "AI FEATURES", bg=BLUE_SOFT, fg=WHITE, size=14)
    headline(d, (48, 100), "AI that respects your\ndata and your budget.", 44, WHITE)
    body(d, (48, 300), "Rule-based diagnosis and local content generation work fully offline. Optional remote AI when you're ready.", 21, "#C7D2E8", max_width=740)
    items = [
        "Business Diagnosis Engine",
        "Priority Action Generator",
        "Local Content Idea Provider",
        "Weekly Recommendation Engine",
    ]
    y = 420
    for t in items:
        panel(d, [48, y, 720, y+60], radius=14, fill=(23,33,58))
        d.rounded_rectangle([72, y+16, 96, y+40], radius=8, fill=BLUE_SOFT)
        d.text((116, y+18), t, font=font(18), fill=WHITE)
        y += 74
    footer(d, "UsahaNaik  ·  LinkedIn Launch Series  ·  Slide 10 / 14")
    save(img, f"{OUT}/10_ai.png")

def slide_tech():
    img, d = new_canvas()
    top_accent(d)
    badge(d, (48, 40), "TECHNOLOGY STACK", size=14)
    headline(d, (48, 90), "Modern, maintainable,\nproduction-grade.", 42)
    cols = [
        ("UI", "Jetpack Compose\nMaterial 3"),
        ("Data", "Room Database\nStateFlow"),
        ("AI", "Rule Engine\nLocal Provider"),
        ("Platform", "Android 8+\nPlay Ready"),
    ]
    x = 48
    for t, s in cols:
        panel(d, [x, 300, x+260, 520], radius=20)
        d.rounded_rectangle([x+20, 322, x+52, 354], radius=8, fill=BLUE)
        d.text((x+20, 380), t, font=font(22), fill=INK)
        body(d, (x+20, 420), s, 17, max_width=220, line_spacing=1.4)
        x += 276
    d.text((48, 560), "minSdk 26  ·  compileSdk 35  ·  100% Kotlin  ·  Clean Architecture", font=font(17, False), fill=OUTLINE)
    footer(d, "UsahaNaik  ·  LinkedIn Launch Series  ·  Slide 11 / 14")
    save(img, f"{OUT}/11_tech.png")

def slide_impact():
    img, d = new_canvas()
    top_accent(d)
    badge(d, (48, 40), "IMPACT", size=14)
    headline(d, (48, 90), "Built for real businesses,\nmeasured in real outcomes.", 40)
    stats = [("10+", "Screens redesigned"), ("100%", "Local-first data"), ("5", "Core workflows"), ("24/7", "On-device AI")]
    x = 48
    for v, l in stats:
        panel(d, [x, 300, x+260, 440], radius=20)
        d.text((x+20, 330), v, font=font(48), fill=BLUE)
        d.text((x+20, 400), l, font=font(17, False), fill=INK_MUTED)
        x += 276
    body(d, (48, 500), "A complete business intelligence suite, redesigned to the Stitch Executive Precision standard — from onboarding to daily operations.", 19, max_width=1000)
    footer(d, "UsahaNaik  ·  LinkedIn Launch Series  ·  Slide 12 / 14")
    save(img, f"{OUT}/12_impact.png")

def slide_architecture():
    img, d = new_canvas()
    top_accent(d)
    badge(d, (48, 40), "ARCHITECTURE", size=14)
    headline(d, (48, 90), "Clean, testable, extensible.", 40)
    layers = [
        ("UI", "Compose Screens · ViewModels · Navigation"),
        ("Domain", "Calculators · Validators · Diagnosis Engine"),
        ("Data", "Room DAOs · Repositories · Mappers"),
        ("Platform", "WorkManager · Notifications · Preferences"),
    ]
    y = 260
    for t, s in layers:
        panel(d, [48, y, 1152, y+64], radius=14)
        d.rounded_rectangle([72, y+12, 112, y+44], radius=8, fill=BLUE)
        d.text((130, y+18), t, font=font(19), fill=INK)
        d.text((260, y+20), s, font=font(15, False), fill=INK_MUTED)
        if y < 260 + 3*64:
            d.text((600, y+66), "▲", font=font(16), fill=OUTLINE)
        y += 64
    d.text((48, 560), "Unidirectional flow:  UI → ViewModel → Domain → Data  (StateFlow observed via Compose)", font=font(15, False), fill=OUTLINE)
    footer(d, "UsahaNaik  ·  LinkedIn Launch Series  ·  Slide 13 / 14")
    save(img, f"{OUT}/13_architecture.png")

def slide_closing():
    img, d = new_canvas(NAVY)
    d.ellipse([-100, 380, 400, 750], fill=(19,27,46))
    d.ellipse([850, -100, 1400, 300], fill=(33,112,228))
    top_accent(d, BLUE_SOFT)
    logo_mark(d, (48, 48), 52)
    headline(d, (48, 250), "Usaha Naik.\nBuilt for the climb.", 56, WHITE)
    body(d, (48, 480), "AI-powered business intelligence for entrepreneurs who want clarity, consistency, and growth.", 22, "#C7D2E8", max_width=800)
    d.rounded_rectangle([48, 560, 420, 614], radius=12, fill=BLUE_SOFT)
    d.text((68, 574), "Follow for the full launch story →", font=font(17), fill=WHITE)
    footer(d, "UsahaNaik  ·  LinkedIn Launch Series  ·  Slide 14 / 14")
    save(img, f"{OUT}/14_closing.png")

def main():
    os.makedirs(OUT, exist_ok=True)
    slide_cover()
    slide_problem()
    slide_solution()
    slide_features()
    slide_dashboard()
    slide_financial()
    slide_planner()
    slide_content()
    slide_reports()
    slide_ai()
    slide_tech()
    slide_impact()
    slide_architecture()
    slide_closing()
    print("All 14 LinkedIn slides generated.")

if __name__ == "__main__":
    main()
