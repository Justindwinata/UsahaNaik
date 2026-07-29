#!/usr/bin/env python3
"""Generate deterministic portfolio mockups and LinkedIn campaign visuals."""

from __future__ import annotations

import argparse
from pathlib import Path

from PIL import Image, ImageDraw, ImageFont


ROOT = Path.cwd().resolve()
MOCKUP_DIR = ROOT / "docs" / "visuals" / "mockups"
LINKEDIN_DIR = ROOT / "docs" / "visuals" / "linkedin_campaign"

CREAM = "#FFF8EF"
SURFACE = "#FFFFFF"
INK = "#182027"
MUTED = "#5B6470"
ORANGE = "#F36F45"
GREEN = "#3C9A6B"
BLUE = "#3467B7"
LAVENDER = "#F0EDFF"
MINT = "#EAF8F0"
YELLOW = "#FFF0C9"
CORAL = "#FFE5DD"
BORDER = "#E8DCCE"


def font(size: int, bold: bool = False) -> ImageFont.FreeTypeFont | ImageFont.ImageFont:
    candidates = [
        "/System/Library/Fonts/Supplemental/Arial Bold.ttf" if bold else "/System/Library/Fonts/Supplemental/Arial.ttf",
        "/System/Library/Fonts/Helvetica.ttc",
        "/Library/Fonts/Arial.ttf",
    ]
    for candidate in candidates:
        try:
            return ImageFont.truetype(candidate, size)
        except OSError:
            continue
    return ImageFont.load_default()


def rounded(draw: ImageDraw.ImageDraw, box: tuple[int, int, int, int], fill: str, radius: int = 28, outline: str | None = None) -> None:
    draw.rounded_rectangle(box, radius=radius, fill=fill, outline=outline or fill, width=2)


def text(draw: ImageDraw.ImageDraw, xy: tuple[int, int], value: str, size: int, fill: str = INK, bold: bool = False) -> None:
    draw.text(xy, value, font=font(size, bold), fill=fill)


def chip(draw: ImageDraw.ImageDraw, xy: tuple[int, int], value: str, fill: str = CORAL, color: str = ORANGE) -> None:
    x, y = xy
    width = max(120, len(value) * 10 + 34)
    rounded(draw, (x, y, x + width, y + 36), fill, radius=18)
    text(draw, (x + 17, y + 8), value, 15, color, True)


def dashboard_panel(draw: ImageDraw.ImageDraw, x: int, y: int, w: int, h: int, mobile: bool = False) -> None:
    rounded(draw, (x, y, x + w, y + h), SURFACE, radius=34, outline=BORDER)
    text(draw, (x + 34, y + 30), "UsahaNaik", 28 if not mobile else 22, INK, True)
    text(draw, (x + 34, y + 65), "Dashboard UMKM lokal-first", 18 if not mobile else 14, MUTED)
    chip(draw, (x + w - 210, y + 28), "Rule-based", MINT, GREEN)
    compact = mobile and h < 520
    card_w = (w - 92) // 2
    card_h = 118 if not mobile else (74 if compact else 92)
    row_gap = 14 if compact else 22
    cards = [
        ("Omzet", "Rp12,5 jt", BLUE),
        ("Profit", "Rp5,5 jt", GREEN),
        ("Health", "72/100", ORANGE),
        ("Konten", "8 ide", BLUE),
    ]
    for index, (label, value, color) in enumerate(cards):
        col = index % 2
        row = index // 2
        cx = x + 34 + col * (card_w + 24)
        cy = y + 115 + row * (card_h + row_gap)
        rounded(draw, (cx, cy, cx + card_w, cy + card_h), "#FFFCF8", radius=22, outline=BORDER)
        text(draw, (cx + 22, cy + 18), label, 15, MUTED, True)
        text(draw, (cx + 22, cy + 48), value, 28 if not mobile else 22, color, True)
    chart_y = y + 115 + 2 * (card_h + row_gap) + 16
    rounded(draw, (x + 34, chart_y, x + w - 34, y + h - 34), LAVENDER, radius=24)
    if not compact:
        text(draw, (x + 58, chart_y + 24), "Revenue vs Expense", 20 if not mobile else 16, INK, True)
    base = y + h - 60
    points = [(x + 70, base - 6), (x + 150, base - 25), (x + 230, base - 18), (x + 310, base - 38), (x + 390, base - 30)]
    if mobile:
        points = [(x + 60, base - 8), (x + 120, base - 32), (x + 180, base - 22), (x + 240, base - 48), (x + 300, base - 38)]
    draw.line(points, fill=GREEN, width=5)
    expense = [(px, py + 38) for px, py in points]
    draw.line(expense, fill=ORANGE, width=5)


def phone_frame(draw: ImageDraw.ImageDraw, x: int, y: int, w: int, h: int) -> None:
    rounded(draw, (x, y, x + w, y + h), "#1E242B", radius=46)
    rounded(draw, (x + 14, y + 14, x + w - 14, y + h - 14), CREAM, radius=36)
    dashboard_panel(draw, x + 28, y + 44, w - 56, h - 88, mobile=True)


def desktop_mockup() -> None:
    img = Image.new("RGB", (1600, 1000), CREAM)
    draw = ImageDraw.Draw(img)
    text(draw, (90, 78), "UsahaNaik", 64, INK, True)
    text(draw, (92, 154), "Local-first business growth planner untuk UMKM", 30, MUTED)
    for i, label in enumerate(["Kotlin", "Jetpack Compose", "Room", "Rule-based Insights"]):
        chip(draw, (92 + i * 215, 220), label, CORAL if i % 2 == 0 else MINT, ORANGE if i % 2 == 0 else GREEN)
    rounded(draw, (90, 315, 1510, 910), "#1F2630", radius=28)
    rounded(draw, (115, 350, 1485, 885), SURFACE, radius=18)
    dashboard_panel(draw, 155, 390, 1290, 455)
    img.save(MOCKUP_DIR / "mockup_desktop.png")


def mobile_mockup() -> None:
    img = Image.new("RGB", (1200, 1600), CREAM)
    draw = ImageDraw.Draw(img)
    text(draw, (90, 90), "UsahaNaik", 58, INK, True)
    text(draw, (92, 160), "Dashboard bisnis UMKM di perangkat lokal.", 29, MUTED)
    phone_frame(draw, 320, 260, 560, 1120)
    chip(draw, (90, 1420), "Mode lokal", CORAL, ORANGE)
    chip(draw, (265, 1420), "Bahasa Indonesia", MINT, GREEN)
    chip(draw, (515, 1420), "Portfolio-ready", LAVENDER, BLUE)
    img.save(MOCKUP_DIR / "mockup_mobile.png")


def showcase_mockup() -> None:
    img = Image.new("RGB", (1600, 900), "#FFF4E8")
    draw = ImageDraw.Draw(img)
    text(draw, (85, 75), "UsahaNaik", 68, INK, True)
    text(draw, (88, 152), "Planner bisnis lokal untuk setup, finance, plan, konten, report, dan reminder.", 28, MUTED)
    dashboard_panel(draw, 90, 250, 870, 540)
    phone_frame(draw, 1050, 130, 380, 680)
    for i, label in enumerate(["Local-first", "Compose", "Room", "Demo Mode", "AI-ready"]):
        chip(draw, (90 + i * 185, 820), label, SURFACE, ORANGE if i in (0, 4) else GREEN)
    img.save(MOCKUP_DIR / "mockup_showcase.png")


def linkedin_asset(name: str, size: tuple[int, int], title: str, subtitle: str, bullets: list[str]) -> None:
    img = Image.new("RGB", size, CREAM)
    draw = ImageDraw.Draw(img)
    w, h = size
    rounded(draw, (48, 48, w - 48, h - 48), SURFACE, radius=36, outline=BORDER)
    text(draw, (88, 88), title, 54 if w > 1200 else 44, INK, True)
    text(draw, (90, 158), subtitle, 25 if w > 1200 else 22, MUTED)
    y = 245
    for index, bullet in enumerate(bullets[:5]):
        fill = [CORAL, MINT, LAVENDER, YELLOW, "#EAF4FF"][index % 5]
        rounded(draw, (90, y, min(w - 90, 90 + len(bullet) * 16 + 70), y + 54), fill, radius=22)
        text(draw, (118, y + 14), bullet, 22, INK, True)
        y += 75
    if w >= 1400:
        dashboard_panel(draw, w - 660, 255, 560, 420, mobile=True)
    else:
        rounded(draw, (90, h - 210, w - 90, h - 95), CORAL, radius=26)
        text(draw, (120, h - 178), "Local-first Android portfolio app", 28, ORANGE, True)
        text(draw, (120, h - 138), "Tidak ada klaim profit pasti.", 22, MUTED)
    img.save(LINKEDIN_DIR / name)


def generate_mockups() -> None:
    MOCKUP_DIR.mkdir(parents=True, exist_ok=True)
    desktop_mockup()
    mobile_mockup()
    showcase_mockup()


def generate_linkedin() -> None:
    LINKEDIN_DIR.mkdir(parents=True, exist_ok=True)
    linkedin_asset(
        "linkedin_cover.png",
        (1600, 900),
        "UsahaNaik",
        "Local-first Android planner untuk UMKM",
        ["Dashboard interaktif", "Finance tracking lokal", "Weekly plan", "Content planner", "Business report"],
    )
    linkedin_asset(
        "linkedin_square_post.png",
        (1080, 1080),
        "UsahaNaik",
        "Portfolio Android App",
        ["Kotlin + Compose", "Room database", "Bahasa Indonesia", "Demo mode", "Rule-based insight"],
    )
    carousel = [
        ("linkedin_carousel_01_intro.png", "Masalah UMKM", "Data usaha sering tersebar dan sulit dipantau.", ["Catatan tidak rapi", "Konten tidak konsisten", "Target belum jelas"]),
        ("linkedin_carousel_02_features.png", "Fitur Utama", "Satu app lokal untuk perencanaan usaha.", ["Setup bisnis", "Finance tracking", "Weekly tasks", "Content calendar"]),
        ("linkedin_carousel_03_tech_stack.png", "Tech Stack", "Dibangun sebagai portfolio Android modern.", ["Kotlin", "Jetpack Compose", "Material 3", "Room", "WorkManager"]),
        ("linkedin_carousel_04_demo.png", "Demo Flow", "Mode demo membuat app mudah dipresentasikan.", ["Load demo data", "Lihat dashboard", "Generate plan", "Buka report"]),
        ("linkedin_carousel_05_closing.png", "Final Result", "Local-first, bilingual, dan siap untuk portfolio.", ["GitHub ready", "Visual package", "Honest limitations", "No hardcoded keys"]),
    ]
    for filename, title, subtitle, bullets in carousel:
        linkedin_asset(filename, (1080, 1080), title, subtitle, bullets)
    linkedin_asset(
        "linkedin_banner.png",
        (1600, 500),
        "UsahaNaik",
        "Android portfolio app untuk UMKM",
        ["Local-first", "Compose UI", "Dashboard", "Report"],
    )


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--target", choices=["mockups", "linkedin", "all"], default="all")
    args = parser.parse_args()
    if args.target in {"mockups", "all"}:
        generate_mockups()
    if args.target in {"linkedin", "all"}:
        generate_linkedin()
    print(f"Generated visual assets: {args.target}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
