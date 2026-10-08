# Rucksack (2026 Resurrection)

## 1. Overview

Rucksack is a cross-platform (Android/iOS) travel preparation app designed to simplify packing through modular, activity-based sub-lists called **Pastimes**. This project is a 2026 "resurrection" and modernization of a legacy Java-based Android application.

## 2. Core Philosophy

Instead of building static lists for every trip, Rucksack utilizes a relational model:

1.  **Master Inventory:** A "Google Keep-style" repository of everything you own.
2.  **Pastimes:** Reusable templates (e.g., _Hockey Officiating_, _Violin Performance_, _Work Trip_) that group items from the Master Inventory.
3.  **Trips:** A specific instance of travel that "merges" selected Pastimes into a single, de-duplicated packing list.

## 3. Technical Stack (2026)

- **Framework:** React Native + Expo
- **Language:** TypeScript
- **State Management:** Zustand (for high-performance list merging)
- **Database:** Expo SQLite (Persistence)
- **Styling:** NativeWind (Tailwind CSS for React Native)
- **Animations:** React Native Reanimated (Parallax effects)
- **Icons:** Lucide-React-Native

## 4. Key View Requirements

### 4.1. Dashboard

- **Empty State:** Shows a briefcase icon with a prompt to create the first trip.
- **Navigation:** Hamburger menu (Drawer) to access Master Inventory and Pastime settings.
- **Action:** Floating Action Button (FAB) to initiate Trip Creation.

### 4.2. Trip Creation

- **Inputs:** Destination (via Google Places API), Departure Date, Return Date.
- **Logic:** Automatically calculates "Nights" to support the Sun/Moon UI elements.

### 4.3. Trip Details (The Parallax View)

- **Header:** Occupies 1/3 of the screen with a photo of the destination.
- **Overlays:** Destination name and duration (X Days / Y Nights with Sun/Moon icons) in the bottom-right corner.
- **Behavior:** Parallax scrolling effect (header moves at 0.5x speed).
- **Sorting:** Unchecked items stay at the top; checked items move to the bottom of their respective categories.

### 4.4. Master Inventory

- **UI:** Continuous, low-friction checklist.
- **Categories:** Items can optionally be grouped (e.g., Toiletries, Electronics).
- **Fast-Add:** Persistent input bar at the bottom for rapid-fire item entry.

## 5. "Check-Back" (Return Home) Logic

To prevent leaving items behind, the app features a "Return Mode":

- On the `endDate`, the app UI flips.
- The list filters to show **only** items that were successfully "Packed-Out."
- User checks them back in to ensure everything brought to the destination returns home.

## 6. Legacy Logic Mapping (Java -> TypeScript)

- **Trip.java:** Translated to `Trip` interface; maintains `nightLength` for UI duration logic.
- **Pastime.java:** Logic preserved for activity-based item grouping; replaces Android `R.drawable` reflection with a static Lucide icon map.
- **PackingList.java:** The `merge()` logic is modernized using JS `reduce` and `Set` to prevent item duplication.
- **PackableItem.java:** Preserves the `compareIncludingPacking` sorting logic.

## 7. Proximity Alerts (Google Maps Sync)

### 7.1. Feature Overview

- **Syncing Locations:** The application allows users to synchronize their "Saved" or "Starred" locations from Google Maps.
- **Cross-Referencing:** Rucksack cross-references these coordinates against the destination coordinates of a planned trip.
- **User Goal:** The primary goal is to alert users when they are near a location they intended to visit.

### 7.2. Configuration & Logic

- **Sync Source:** Utilizes OAuth integration with the Google People API to access starred points, or a manual import of Google Takeout JSON/KML files.
- **Buffer Zone:** Includes a user-configurable slider in Settings (e.g., 5km to 50km) to define proximity.
- **The Proximity Check:**
  - When a trip is created or updated, the app calculates the distance between the destination (from Google Places API) and all saved locations.
  - **Formula:** Uses the **Haversine Formula** to calculate the great-circle distance between two points on a sphere:
    $$d = 2r \\arcsin\\left(\\sqrt{\\sin^2\\left(\\frac{\\phi_2 - \\phi_1}{2}\\right) + \\cos(\\phi_1) \\cos(\\phi_2) \\sin^2\\left(\\frac{\\lambda_2 - \\lambda_1}{2}\\right)}\\right)$$
    _(Where $\\phi$ is latitude, $\\lambda$ is longitude, and $r$ is the Earth's radius)._

### 7.3. UI/UX Integration

- **Trip Creation Alert:** If a match is found during the trip creation flow, a "Nearby Interest" card appears in the UI.
- **Notification:** A local push notification is scheduled for the `startDate`, reminding the user they will be near a saved location.
- **Master Inventory Impact:** If a nearby location is detected, the app may suggest adding a specific activity-based "Pastime" (e.g., suggesting "Hiking" if the spot is a trailhead).

### 7.4. Active Trip Geofencing (Passive Tracking)

- **Trigger Window:** Passive location tracking only activates between the `startDate` and `endDate` of a planned trip.
- **Frequency:** The app uses a low-power background task that polls device location approximately every 60 minutes (user-configurable).
- **Logic:**
  - The app compares current GPS coordinates against the synced "Starred" locations list.
  - If the user enters the defined Buffer Zone of a starred location, a real-time notification is triggered.
- **Battery Optimization:**
  - Uses **Expo TaskManager** to define a background execution region.
  - The background task is automatically de-registered once the `endDate` passes to ensure zero activity when the user returns home.

### 7.5. Privacy & Permissions

- **Permission Flow:** Users must explicitly grant "Always Allow" location access and "Notification" permissions during the trip creation flow.
- **Transparency:** A persistent notification or dashboard banner indicates when "Passive Proximity Monitoring" is active.

### 7.6. Technical Implementation Notes (Section 6 Mapping)

- **useLocationStore:** A Zustand store created to house the user's starred coordinates.
- **ProximityManager.ts:** A new service that handles Haversine Formula calculations and interacts with the store to filter relevant points based on the active trip's radius.
- **Background Services:** Implemented via **Expo Location**, **Background Fetch**, and **Expo Notifications** for high performance with minimal battery drain.

## 8. Visual Identity & Branding

### 8.1. Launcher Icon

- **Source Asset:** The app launcher utilizes the SVG file located at `/assets/rucksack-icon.svg`.
- **Composition:** The icon must be centered within a circular background.
- **Background Color:** The circular container uses the brand-specific "Rucksack Orange" (Hex: `#FFAD1C`).
- **Foreground Color:** The icon itself should be white or a shade of white that contrasts well with the orange background.

### 8.2. Core Color Palette

- **Rucksack Orange (#FFAD1C):** This is the primary brand color and must be used consistently across the following UI elements:
  - **Menu Bar:** The top navigation or menu bar background color.
  - **Accent Color:** Used for the Floating Action Button (FAB), active tab indicators, and primary action buttons.
  - **Thematic Elements:** Integrated into the Sun/Moon icons or duration overlays within the Parallax View to maintain visual cohesion.

### 8.3. Implementation (NativeWind/Tailwind)

- **Configuration:** The `#FFAD1C` value should be defined as the `primary` or `brand-orange` color in the `tailwind.config.js` to ensure consistent application across the TypeScript/React Native stack.

---

_Created by Scott Johnson | 2026_
