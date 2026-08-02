"""Generate mockups and portfolio assets from captured screenshots."""
import os, sys
from PIL import Image, ImageDraw, ImageOps
sys.path.insert(0, os.path.dirname(__file__))
from campaign_style import *

ROOT = os.path.join(os.path.dirname(__file__), "..")
SHOT_DIR = os.path.join(ROOT, "docs", "evidence", "screenshots", "stitch_final")
MOCK_DIR = os.path.join(ROOT, "docs", "visuals", "mockups")
THUMB_DIR = os.path.join(ROOT, "docs", "visuals", "thumbnail")
BANNER_DIR = os.path.join(ROOT, "docs", "visuals", "banner")
GH_DIR = os.path.join(ROOT, "docs", "visuals", "github")

shots = [
    os.path.join(SHOT_DIR, "screen_welcome.png"),
    os.path.join(SHOT_DIR, "screen_current.png"),
    os.path.join(SHOT_DIR, "screenshot1.png"),
]

def load_first_valid():
    imgs = []
    for p in shots:
        if os.path.exists(p):
            imgs.append(Image.open(p).convert("RGB"))
    return imgs

def phone_mockup(base, screenshot, x, y, w=220, h=450):
    base_draw = ImageDraw.Draw(base)
    base_draw.rounded_rectangle([x, y, x+w, y+h], radius=36, fill="#0F172A")
    screen = ImageOps.fit(screenshot, (w-24, h-28), method=Image.Resampling.LANCZOS)
    base.paste(screen, (x+12, y+14))
    base_draw.rounded_rectangle([x+w/2-36, y+8, x+w/2+36, y+18], radius=8, fill="#1E293B")

def tablet_mockup(base, screenshot, x, y, w=360, h=250):
    d = ImageDraw.Draw(base)
    d.rounded_rectangle([x, y, x+w, y+h], radius=28, fill="#111827")
    screen = ImageOps.fit(screenshot, (w-22, h-22), method=Image.Resampling.LANCZOS)
    base.paste(screen, (x+11, y+11))

def desktop_mockup(base, screenshot, x, y, w=520, h=320):
    d = ImageDraw.Draw(base)
    d.rounded_rectangle([x, y, x+w, y+h], radius=18, fill="#111827")
    screen = ImageOps.fit(screenshot, (w-20, h-20), method=Image.Resampling.LANCZOS)
    base.paste(screen, (x+10, y+10))
    d.rectangle([x+w/2-80, y+h+4, x+w/2+80, y+h+18], fill="#CBD5E1")
    d.rectangle([x+w/2-22, y+h-2, x+w/2+22, y+h+34], fill="#94A3B8")

def make_android(imgs):
    img, d = new_canvas()
    top_accent(d)
    headline(d, (48, 60), "Android Mockup", 36)
    phone_mockup(img, imgs[0], 150, 140)
    phone_mockup(img, imgs[1], 430, 120)
    phone_mockup(img, imgs[2], 710, 140)
    footer(d, "UsahaNaik  ·  Android Device Showcase")
    save(img, os.path.join(MOCK_DIR, "android_mockup.png"))

def make_tablet(imgs):
    img, d = new_canvas()
    top_accent(d)
    headline(d, (48, 60), "Tablet Mockup", 36)
    tablet_mockup(img, imgs[1], 220, 180, 760, 430)
    footer(d, "UsahaNaik  ·  Tablet Presentation")
    save(img, os.path.join(MOCK_DIR, "tablet_mockup.png"))

def make_desktop(imgs):
    img, d = new_canvas()
    top_accent(d)
    headline(d, (48, 60), "Desktop Dashboard", 36)
    desktop_mockup(img, imgs[1], 160, 180, 880, 430)
    footer(d, "UsahaNaik  ·  Desktop Adaptation")
    save(img, os.path.join(MOCK_DIR, "desktop_mockup.png"))

def make_multi(imgs):
    img, d = new_canvas(NAVY)
    top_accent(d, BLUE_SOFT)
    headline(d, (48, 50), "Multi-Device Experience", 38, WHITE)
    desktop_mockup(img, imgs[1], 310, 180, 620, 300)
    tablet_mockup(img, imgs[2], 80, 280, 300, 200)
    phone_mockup(img, imgs[0], 880, 180, 180, 360)
    footer(d, "UsahaNaik  ·  Multi-Device Showcase")
    save(img, os.path.join(MOCK_DIR, "multi_device_mockup.png"))

def make_thumbnail(imgs):
    img = Image.new("RGB", (1280, 720), NAVY)
    d = ImageDraw.Draw(img)
    top_accent(d, BLUE_SOFT)
    headline(d, (60, 90), "UsahaNaik", 62, WHITE)
    body(d, (60, 190), "AI-Powered Business Intelligence App", 26, "#C7D2E8", max_width=560)
    phone_mockup(img, imgs[0], 760, 90, 180, 360)
    phone_mockup(img, imgs[1], 930, 140, 180, 360)
    badge(d, (60, 270), "PORTFOLIO SHOWCASE", bg=BLUE_SOFT, fg=WHITE)
    save(img, os.path.join(THUMB_DIR, "portfolio_thumbnail.png"))

def make_banner(imgs):
    img = Image.new("RGB", (1600, 600), SURFACE)
    d = ImageDraw.Draw(img)
    top_accent(d)
    headline(d, (80, 110), "UsahaNaik", 72)
    body(d, (80, 220), "AI-powered business intelligence for modern entrepreneurs", 28, max_width=700)
    desktop_mockup(img, imgs[1], 860, 90, 620, 360)
    save(img, os.path.join(BANNER_DIR, "readme_banner.png"))

def make_github_preview(imgs):
    img = Image.new("RGB", (1280, 640), NAVY)
    d = ImageDraw.Draw(img)
    top_accent(d, BLUE_SOFT)
    headline(d, (60, 100), "UsahaNaik", 64, WHITE)
    body(d, (60, 220), "LinkedIn-ready product campaign assets, UI redesign, and real runtime captures.", 24, "#C7D2E8", max_width=560)
    phone_mockup(img, imgs[0], 830, 110, 170, 340)
    phone_mockup(img, imgs[1], 980, 160, 170, 340)
    save(img, os.path.join(GH_DIR, "github_social_preview.png"))

def make_collage(imgs):
    img = Image.new("RGB", (1400, 900), SURFACE)
    d = ImageDraw.Draw(img)
    top_accent(d)
    headline(d, (60, 50), "UsahaNaik Showcase", 44)
    positions = [(60, 160, 380, 720), (420, 160, 740, 720), (780, 160, 1100, 720)]
    for shot, pos in zip(imgs, positions):
        card = ImageOps.fit(shot, (pos[2]-pos[0], pos[3]-pos[1]), method=Image.Resampling.LANCZOS)
        img.paste(card, (pos[0], pos[1]))
        d.rounded_rectangle(pos, radius=18, outline="#E2E8F0", width=2)
    save(img, os.path.join(GH_DIR, "showcase_collage.png"))

def main():
    os.makedirs(MOCK_DIR, exist_ok=True)
    os.makedirs(THUMB_DIR, exist_ok=True)
    os.makedirs(BANNER_DIR, exist_ok=True)
    os.makedirs(GH_DIR, exist_ok=True)
    imgs = load_first_valid()
    if not imgs:
        raise SystemExit("No screenshots found")
    while len(imgs) < 3:
        imgs.append(imgs[-1])
    make_android(imgs)
    make_tablet(imgs)
    make_desktop(imgs)
    make_multi(imgs)
    make_thumbnail(imgs)
    make_banner(imgs)
    make_github_preview(imgs)
    make_collage(imgs)
    print("Mockups and portfolio assets generated.")

if __name__ == "__main__":
    main()
