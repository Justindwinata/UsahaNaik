"""Shared campaign styling helpers for Usaha Naik LinkedIn visual package."""
from PIL import Image, ImageDraw, ImageFont

# Brand palette from app theme (Color.kt)
PRIMARY = "#131B2E"          # Deep Navy (primary container)
NAVY = "#0F172A"             # Executive navy
BLUE = "#0058BE"             # Secondary / accent blue
BLUE_LIGHT = "#D8E2FF"       # Secondary fixed
BLUE_SOFT = "#2170E4"        # Secondary container
ORANGE = "#D95F00"           # On tertiary container (critical orange)
SURFACE = "#F7F9FB"          # Background
WHITE = "#FFFFFF"
INK = "#191C1E"
INK_MUTED = "#45464D"
OUTLINE = "#76777D"
GREEN = "#0058BE"

W, H = 1200, 675  # LinkedIn slide canvas 16:9

def font(size, bold=True):
    """Return Inter-style sans font, fallback to DejaVu."""
    import os
    candidates = [
        ("/System/Library/Fonts/Supplemental/Arial.ttf", "Arial"),
        ("/System/Library/Fonts/Supplemental/Arial Bold.ttf", "Arial Bold"),
        ("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf", None),
    ]
    path = None
    for p, _ in candidates:
        if os.path.exists(p):
            path = p
            break
    if path is None:
        path = "/Library/Fonts/Arial.ttf"
    try:
        if "Bold" in path or bold is False:
            return ImageFont.truetype(path, size)
        return ImageFont.truetype("/System/Library/Fonts/Supplemental/Arial Bold.ttf", size)
    except Exception:
        return ImageFont.load_default()

def new_canvas(bg=SURFACE):
    img = Image.new("RGB", (W, H), bg)
    d = ImageDraw.Draw(img)
    return img, d

def rounded_rect(d, box, radius, fill=None, outline=None, width=1):
    d.rounded_rectangle(box, radius=radius, fill=fill, outline=outline, width=width)

def panel(d, box, radius=24, fill=WHITE, outline="#E2E8F0"):
    rounded_rect(d, box, radius, fill=fill, outline=outline, width=1)

def label(d, xy, text, size=15, fill=BLUE, bold=True, tracking=3):
    f = font(size, bold)
    d.text(xy, text, font=f, fill=fill)

def headline(d, xy, text, size=44, fill=NAVY, bold=True, max_width=None, line_spacing=1.15):
    f = font(size, bold)
    # word wrap
    words = text.split()
    lines, cur = [], ""
    for w in words:
        test = (cur + " " + w).strip()
        if d.textlength(test, font=f) > (max_width or 1000):
            lines.append(cur)
            cur = w
        else:
            cur = test
    if cur:
        lines.append(cur)
    x, y = xy
    for ln in lines:
        d.text((x, y), ln, font=f, fill=fill)
        y += int(size * line_spacing)
    return y

def body(d, xy, text, size=20, fill=INK_MUTED, max_width=900, line_spacing=1.4):
    f = font(size, bold=False)
    words = text.split()
    lines, cur = [], ""
    for w in words:
        test = (cur + " " + w).strip()
        if d.textlength(test, font=f) > max_width:
            lines.append(cur)
            cur = w
        else:
            cur = test
    if cur:
        lines.append(cur)
    x, y = xy
    for ln in lines:
        d.text((x, y), ln, font=f, fill=fill)
        y += int(size * line_spacing)
    return y

def footer(d, text="UsahaNaik  ·  AI-Powered Business Intelligence"):
    f = font(13, bold=False)
    d.line([(48, H-44), (W-48, H-44)], fill="#E2E8F0", width=1)
    d.text((48, H-34), text, font=f, fill=OUTLINE)

def top_accent(d, color=BLUE, h=6):
    d.rectangle([0, 0, W, h], fill=color)

def logo_mark(d, xy, size=48, bg=NAVY, fg=WHITE, text="UN"):
    x, y = xy
    d.rounded_rectangle([x, y, x+size, y+size], radius=size//4, fill=bg)
    f = font(int(size*0.42), True)
    tw = d.textlength(text, font=f)
    d.text((x + (size-tw)/2, y + (size-24)/2 - 2), text, font=f, fill=fg)

def badge(d, xy, text, bg=BLUE_LIGHT, fg=BLUE, size=14):
    f = font(size)
    tw = d.textlength(text, font=f)
    x, y = xy
    w = int(tw) + 24
    rounded_rect(d, [x, y, x+w, y+34], 17, fill=bg)
    d.text((x+12, y+9), text, font=f, fill=fg)

def save(img, path):
    img.save(path, "PNG")
    print(f"saved: {path}")
