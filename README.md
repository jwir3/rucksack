# Rucksack

A cross-platform (Android/iOS) travel preparation app built with Expo + React Native. See `DESIGN.md` for full feature specifications.

## Prerequisites

- [asdf](https://asdf-vm.com/) with the `nodejs` and `yarn` plugins
- [Android Studio](https://developer.android.com/studio) with an emulator configured (for local Android builds)
- [Xcode](https://developer.apple.com/xcode/) (for iOS builds, macOS only)
- An [Expo account](https://expo.dev/signup) (free) — only required for EAS cloud builds; local builds need no account (see below)

## Setup

```bash
# Install the correct Node version (defined in .tool-versions)
asdf install

# Install dependencies
yarn install

# Install EAS CLI globally (one-time)
yarn global add eas-cli
```

**Cloud builds only** — if you plan to use EAS cloud builds, also run:

```bash
eas login           # sign in to / create a free Expo account
eas build:configure # links the project to your account
```

This is not needed for local builds (see below).

## Local Development Workflow

The app uses native modules (`react-native-worklets`, `react-native-reanimated`) that are not available in Expo Go, so it must always be run as a native build. **Do not use `expo start` or `expo start --android`** — these open Expo Go and the app will fail to load.

### How builds and reloads work

There are two distinct layers:

| Layer | What it is | When it recompiles |
|---|---|---|
| **Native build** | Gradle/Xcode compiling all native modules into an APK/app | Only when native dependencies change |
| **JS bundle** | Metro bundling your TypeScript/React code | On every save (hot reload) |

Day-to-day development only touches the JS layer — Metro watches your source files and pushes updates to the running app in under a second. You only need to trigger a full native rebuild when you add or remove a package that has native code.

---

### First run (cold build)

The first build compiles every native dependency from source. **This takes 5–15 minutes** and is normal — it is the same experience as any native Android app. Subsequent native builds are much faster due to Gradle's incremental build cache.

Before building, ensure the native `android/` directory is generated from the current `app.json`:

```bash
# Generate (or regenerate) the native Android project only — skips CocoaPods/iOS
npx expo prebuild --platform android

# If the android/ directory already exists and may be stale (e.g. after changing
# app.json settings like newArchEnabled), force a clean regeneration:
npx expo prebuild --clean --platform android
```

Then start an Android emulator via Android Studio's Device Manager and run:

```bash
yarn android
```

Gradle compiles the native project, installs the APK on the emulator, and Metro starts serving the JS bundle. The app launches automatically.

> **iOS note:** iOS builds are not in scope for now. Always use `--platform android` with `expo prebuild` to avoid triggering a CocoaPods install.

---

### Day-to-day development (hot reload)

Once the native app is installed on the emulator, Metro handles all JS changes automatically:

```bash
# If the Metro bundler stopped but the app is still installed on the emulator:
yarn start

# Then in the running app, shake the device (or press Cmd+M in the emulator)
# to open the dev menu if needed.
```

**You do not need to rerun `yarn android` just to see JS changes** — save a file and the app updates within a second or two.

---

### When to do a full native rebuild

Run `npx expo prebuild --clean --platform android` followed by `yarn android` when you:

- Add or remove a package that includes native Android code
- Change `app.json` settings that affect the native project (e.g. permissions, `newArchEnabled`)
- Change `babel.config.js` or `metro.config.js`
- Pull changes from someone else that modified `package.json` native dependencies

You do **not** need a native rebuild for:
- Editing any `.tsx` / `.ts` / `.js` source files
- Adding or changing NativeWind styles
- Updating Zustand stores or other pure-JS logic

---

### Debugging

#### React Native DevTools (recommended)

Press `j` in the Metro terminal to open the built-in React Native DevTools in your browser — this gives you a full Chrome-style debugger with breakpoints, component inspector, and network tab.

#### In-app developer menu

Shake the emulator (or press `Cmd+M` on the Android emulator) to open the dev menu:

- **Reload** — force a full JS bundle refresh
- **Open DevTools** — same as pressing `j` in Metro
- **Toggle Element Inspector** — tap any UI element to inspect its styles and layout

#### Logs

```bash
# Stream all native Android logs (verbose)
adb logcat

# Filter to just React Native output
adb logcat -s ReactNative ReactNativeJS

# Clear the log buffer first to reduce noise
adb logcat -c && adb logcat -s ReactNative ReactNativeJS
```

#### Common issues

| Symptom | Fix |
|---|---|
| Red error screen on launch | Check Metro terminal for the JS error; fix and save to auto-reload |
| App not updating after save | Press `r` in the Metro terminal to force a reload |
| Metro can't connect to emulator | Ensure the emulator is running before starting Metro; try `adb reverse tcp:8081 tcp:8081` |
| Stale native build acting oddly | Stop Metro, run `yarn android` again for a clean native rebuild |
| Gradle build fails after pulling changes | Run `yarn install` first to sync new native dependencies |

## Building for Distribution (EAS Build)

Builds are handled by [EAS Build](https://docs.expo.dev/build/introduction/). Build profiles are defined in `eas.json`.

### Do I need an Expo account?

| Build method | Expo account | Notes |
|---|---|---|
| `eas build` (cloud) | Yes (free tier: 30 min/month) | Expo's servers compile the app |
| `eas build --local` | No | Builds on your machine using Android Studio / Xcode |
| `eas submit` | Yes | Uploads the finished artifact to the store |

> **Recommendation:** Use `--local` builds during development since Android Studio is already set up locally. Switch to cloud builds for CI or when you don't want to wait on a local Gradle compile.

---

### First-Time Cloud Setup (one-time only)

```bash
# 1. Install EAS CLI globally
yarn global add eas-cli

# 2. Sign in to your Expo account (create one free at expo.dev if needed)
eas login

# 3. Verify you are logged in
eas whoami

# 4. Link this project to your Expo account (writes the projectId into app.json)
eas build:configure
```

---

### Build Profiles

Three profiles are defined in `eas.json`:

| Profile | Output | Purpose |
|---|---|---|
| `development` | `.apk` (debug) | Full native dev build with Metro hot reload |
| `preview` | `.apk` | Shareable test build — sideload directly onto a device |
| `production` | `.aab` | Signed release build ready for the Play Store / App Store |

`production` builds have `autoIncrement: true`, so the build number is bumped automatically on every run. The app version (`1.0.0` etc.) is managed remotely via `appVersionSource: "remote"` and updated in the [Expo dashboard](https://expo.dev) rather than in `app.json`.

---

### Android Builds

```bash
# --- Local builds (no Expo account required) ---

# Preview APK — install directly on a device or emulator
eas build --profile preview --platform android --local

# Production AAB — for the Play Store
eas build --profile production --platform android --local


# --- Cloud builds (Expo account required) ---

# Preview APK
eas build --profile preview --platform android

# Production AAB
eas build --profile production --platform android

# Check the status of running or recent builds
eas build:list --platform android

# Download the most recent build artifact
eas build:download --platform android
```

#### Installing a local APK on the emulator

```bash
# After a local --profile preview build, install the output APK:
adb install /path/to/output.apk
```

#### Submitting to the Play Store

```bash
# Upload the latest production build to the Play Store internal track
eas submit --platform android

# Or point to a specific build
eas submit --platform android --id <build-id>
```

> Requires a [Google Play Developer account](https://play.google.com/console/signup) ($25 one-time) and a service account key configured in the Expo dashboard.

---

### iOS Builds

```bash
# --- Local builds (macOS + Xcode required, no Expo account needed) ---

# Simulator build
eas build --profile development --platform ios --local

# Production IPA
eas build --profile production --platform ios --local


# --- Cloud builds (Expo account required) ---

# Production IPA
eas build --profile production --platform ios

# Check status / download
eas build:list --platform ios
eas build:download --platform ios
```

#### Submitting to App Store Connect

Before submitting, fill in the iOS section of `eas.json → submit.production.ios`:

```json
"ios": {
  "appleId": "scott@chromatic.com",
  "ascAppId": "<App Store Connect App ID>",
  "appleTeamId": "<Your Apple Team ID>"
}
```

Then run:

```bash
eas submit --platform ios
```

> Requires an [Apple Developer Program](https://developer.apple.com/programs/) membership ($99/year).

---

### Code Signing

EAS manages code signing automatically during cloud builds:

- **Android** — generates and stores a keystore on Expo's servers. You can also supply your own via `eas credentials`.
- **iOS** — handles provisioning profiles and distribution certificates via your Apple Developer account credentials.

For local builds, you must supply your own credentials:

```bash
# Manage credentials interactively
eas credentials
```

---

### Store Developer Accounts

These are separate from Expo and required regardless of build method:

| Store | Account | Cost |
|---|---|---|
| Google Play Store | [Google Play Developer](https://play.google.com/console/signup) | $25 one-time |
| Apple App Store | [Apple Developer Program](https://developer.apple.com/programs/) | $99/year |

## Tech Stack

| Concern | Library |
|---|---|
| Framework | Expo ~54 / React Native 0.81 |
| Language | TypeScript |
| Styling | NativeWind v4 (Tailwind CSS v3) |
| State | Zustand |
| Database | Expo SQLite |
| Animations | React Native Reanimated 4 |
| Navigation | React Navigation Drawer v7 |
| Icons | Lucide React Native |

## Known Configuration Notes

- **NativeWind requires Tailwind v3** — do not upgrade `tailwindcss` to v4; NativeWind 4.x is incompatible with it.
- **New Architecture is enabled** (`newArchEnabled: true`) — required by `react-native-worklets@0.5.1`. All package versions are pinned to Expo 54's exact expected values via `expo install`; do not upgrade `react-native-worklets` independently as newer versions (0.8.x) have a TurboModule API mismatch with RN 0.81.5.
- **`expo run:*` not `expo start`** — the app uses native modules absent from Expo Go; always build natively.
- All package versions are pinned to Expo 54's expected versions. Use `yarn expo install <package>` (not plain `yarn add`) to add new Expo-ecosystem packages so the correct version is resolved automatically.
