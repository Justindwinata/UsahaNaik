"""Generate LinkedIn carousel slides with premium visual treatment."""
import os, sys, math
sys.path.insert(0, os.path.dirname(__file__))
from campaign_style import *

OUT = os.path.join(os.path.dirname(__file__), "..", "docs", "visuals", "linkedin")

def add_mockup_phone(d, base, shot, x, y, w=180, h=360):
    """Add a phone mockup to the image."""
    screen = ImageOps.fit(shot, (w-20, h-24), method=Image.Resampling.LANCZOS)
    d.rounded_rectangle([x, y, x+w, y+h], radius=32, fill=NAVY)
    base.paste(screen, (x+10, y+12))

def add_bento_grid(d, x, y, cells=4):
    """Add a modern bento grid layout element."""
    for i in range(cells):
        bx = x + (i * 140)
        d.rounded_rectangle([bx, y, bx+130, y+120], radius=16, fill=SURFACE)

def add_bar_chart(d, x, y, w=200, h=100):
    """Add a simple bar chart."""
    vals = [60, 80, 45, 90, 70]
    bar_w = w // len(vals) - 8
    for i, v in enumerate(vals):
        bx = x + i * (bar_w + 8)
        bh = int(h * v / 100)
        d.rounded_rectangle([bx, y+h-bh, bx+bar_w, y+h], radius=6, fill=BLUE if i != 2 else ORANGE)

def add_sparkline(d, x, y, w=200, h=40):
    """Add a sparkline trend."""
    points = [20, 35, 28, 50, 45, 65, 58]
    coords = [(x + i * (w // (len(points)-1)), y + h - int(p * h / 70)) for i, p in enumerate(points)]
    for i in range(len(coords)-1):
        d.line([coords[i], coords[i+1]], fill=BLUE, width=2)

def slide_intro():
    img, d = new_canvas(NAVY)
    d.ellipse([900, -100, 1400, 400], fill=(33,112,228))
    d.ellipse([-100, 400, 300, 750], fill=(19,27,46))
    logo_mark(d, (50, 50), 48)
    f = font(16)
    d.text((110, 60), "USAHANAIK", font=f, fill=WHITE)
    badge(d, (50, 220), "LINKEDIN LAUNCH", bg=BLUE_SOFT, fg=WHITE)
    headline(d, (50, 290), "This is how\nSMEs win.", 62, WHITE)
    body(d, (50, 500), "A 14-slide deep-dive into the AI-powered business intelligence app built for Indonesian entrepreneurs.", 20, "#C7D2E8", max_width=720)
    footer(d, "Slide 01/04  ·  UsahaNaik  ·  LinkedIn Campaign")
    save(img, f"{OUT}/campaign_intro.png")

def slide_showcase():
    img, d = new_canvas()
    top_accent(d)
    headline(d, (50, 60), "UsahaNaik at a Glance", 40)
    # KPI cards row
    kpis = [("Revenue", "Rp 42.9M", BLUE), ("Profit", "Rp 24.5M", NAVY), ("Tasks", "75%", BLUE), ("Ideas", "120+", NAVY)]
    x = 50
    for label, value, color in kpis:
        d.rounded_rectangle([x, 160, x+260, 260], radius=16, fill=WHITE, outline="#E2E8F0")
        d.text((x+20, 180), label, font=font(15, False), fill=INK_MUTED)
        d.text((x+20, 210), value, font=font(28), fill=color)
        x += 276
    # Chart section
    d.rounded_rectangle([50, 300, 620, 550], radius=20, fill=WHITE, outline="#E2E8F0")
    d.text((70, 320), "Revenue Trend", font=font(17), fill=NAVY)
    add_bar_chart(d, 80, 380, 520, 120)
    # Sparkline section
    d.rounded_rectangle([650, 300, 1160, 550], radius=20, fill=WHITE, outline="#E2E8F0")
    d.text((670, 320), "Weekly Progress", font=font(17), fill=NAVY)
    add_sparkline(d, 680, 380, 450, 120)
    footer(d, "Slide 02/04  ·  UsahaNaik  ·  LinkedIn Campaign")
    save(img, f"{OUT}/campaign_showcase.png")

def slide_story():
    img, d = new_canvas()
    top_accent(d)
    headline(d, (50, 60), "The Story Behind UsahaNaik", 40)
    timeline = [
        ("Week 1", "Stitch Reference Analysis", "Analyzed 10 screens, extracted design tokens"),
        ("Week 2", "UI Modernization", "Redesigned auth, onboarding, dashboard"),
        ("Week 3", "Feature Completion", "Financial, planner, content, reports"),
        ("Week 4", "Production Polish", "Accessibility, runtime QA, Play Store prep"),
    ]
    y = 160
    for i, (period, title, desc) in enumerate(timeline):
        x = 80 if i % 2 == 0 else 450
        panel(d, [x, y, x+340, y+140], radius=20)
        d.rounded_rectangle([x+20, y+20, x+270, y+52], radius=8, fill=BLUE_LIGHT)
        d.text((x+30, y+26), period, font=font(14), fill=BLUE)
        d.text((x+20, y+66), title, font=font(17), fill=INK)
        d.text((x+20, y+92), desc, font=font(13, False), fill=INK_MUTED)
        y += 155
        if i == 1:
            y = 160
    footer(d, "Slide 03/04  ·  UsahaNaik  ·  LinkedIn Campaign")
    save(img, f"{OUT}/campaign_story.png")

def slide_cta():
    img, d = new_canvas(NAVY)
    d.ellipse([900, -100, 1400, 400], fill=(33,112,228))
    logo_mark(d, (500, 120), 72)
    headline(d, (500, 230), "Follow the\nbuild journey.", 52, WHITE)
    body(d, (500, 420), "14 slides. Real screenshots. Actual code.", 20, "#C7D2E8", max_width=500)
    d.rounded_rectangle([500, 510, 880, 570], radius=16, fill=BLUE_SOFT)
    d.text((580, 526), "Follow for more  →", font=font(18), fill=WHITE)
    footer(d, "Slide 04/04  ·  UsahaNaik  ·  LinkedIn Campaign")
    save(img, f"{OUT}/campaign_cta.png")

def main():
    os.makedirs(OUT, exist_ok=True)
    slide_intro()
    slide_showcase()
    slide_story()
    slide_cta()
    print("4 LinkedIn campaign slides generated.")

if __name__ == "__main__":
    main()
