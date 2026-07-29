#!/usr/bin/env python3
"""Capture Android screenshots when an emulator/device is attached.

The script never fabricates screenshots. If no device is attached, it exits
with guidance and leaves the screenshot folder ready for manual capture.
"""

from __future__ import annotations

import subprocess
import sys
from pathlib import Path


PROJECT_ROOT = Path.cwd().resolve()
OUTPUT_DIR = PROJECT_ROOT / "docs" / "evidence" / "screenshots" / "final"
SUGGESTED_NAMES = [
    "01_dashboard.png",
    "02_finance_form.png",
    "03_weekly_plan.png",
    "04_content_planner.png",
    "05_report.png",
    "06_profile_reminders.png",
]


def adb_devices() -> list[str]:
    try:
        output = subprocess.check_output(["adb", "devices"], text=True)
    except FileNotFoundError:
        print("adb is not available on PATH. Open Android Studio or install Android platform-tools.")
        return []
    devices: list[str] = []
    for line in output.splitlines()[1:]:
        parts = line.split()
        if len(parts) >= 2 and parts[1] == "device":
            devices.append(parts[0])
    return devices


def main() -> int:
    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
    devices = adb_devices()
    if not devices:
        print("No Android emulator/device is attached.")
        print(f"Screenshot folder is ready: {OUTPUT_DIR}")
        print("Run the app manually, then capture screenshots with names:")
        for name in SUGGESTED_NAMES:
            print(f"  - {name}")
        return 2

    device = devices[0]
    output_path = OUTPUT_DIR / "manual_capture_current_screen.png"
    subprocess.check_call(
        ["adb", "-s", device, "exec-out", "screencap", "-p"],
        stdout=output_path.open("wb"),
    )
    print(f"Captured current device screen from {device}: {output_path}")
    print("Rename the screenshot according to the final screenshot checklist if needed.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
