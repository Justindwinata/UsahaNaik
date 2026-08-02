# UsahaNaik - How to Run

## Quick Start (3 commands)

```bash
# 1. Clone
git clone https://github.com/Justindwinata/UsahaNaik.git && cd UsahaNaik

# 2. Build & Install
ANDROID_HOME=$HOME/Library/Android/sdk ./gradlew installDebug

# 3. Launch
adb shell am start -n com.justindwinata.usahanaik/.MainActivity
```

---

## Prerequisites Checklist

- [ ] Android Studio Hedgehog (2023.3.1) or newer
- [ ] Android SDK with API 35 (Android 15)
- [ ] JDK 17 or newer
- [ ] Working Android Emulator or Physical Device

## Step-by-Step Installation

### Step 1: Get the Code

```bash
git clone https://github.com/Justindwinata/UsahaNaik.git
cd UsahaNaik
```

### Step 2: Configure Environment (if needed)

```bash
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```

### Step 3: Build the App

```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Install on connected device/emulator
./gradlew installDebug
```

### Step 4: Launch the App

```bash
# From command line
adb shell am start -n com.justindwinata.usahanaik/.MainActivity

# Or open in Android Studio and click Run ▶️
```

## Testing the Modernized UI

1. **Welcome Screen** - Check Stitch reference styling
2. **Login** - Test modern input fields with focus rings
3. **Dashboard** - Verify KPI cards, charts, and layout
4. **Financial** - Test income/expense tracking
5. **Planner** - Check weekly goals and tasks
6. **Content** - Verify idea generation and scheduling
7. **Reports** - Check KPI overview and export
8. **Settings** - Test modernized navigation

## Troubleshooting

| Issue | Solution |
|-------|----------|
| SDK not found | Set `ANDROID_HOME` env variable |
| No devices | Run `adb kill-server && adb start-server` |
| Gradle sync fails | Use `File` → `Invalidate Caches` in Android Studio |
| Build errors | Run `./gradlew clean` then rebuild |

## What's Included

✅ UI Modernization - 15 commits  
✅ LinkedIn Campaign - 18 slides + mockups  
✅ Visual Package - 1.5MB ZIP with assets  
✅ Real Screenshots - 3 runtime captures  

## Documentation

- `docs/STITCH_REFERENCE_ANALYSIS.md` - Design system reference
- `docs/PROGRESS_PHASE_2.md` - Implementation progress
- `CAMPAIGN_DESIGN_ANALYSIS.md` - Campaign reference analysis

---

**Build Status**: ✅ Successful  
**Push Status**: ✅ All commits pushed  
**Repository**: https://github.com/Justindwinata/UsahaNaik.git
