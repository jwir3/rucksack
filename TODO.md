# Rucksack — Implementation TODO

## Data Layer

- [ ] Define TypeScript interfaces: `Trip`, `Pastime`, `PackableItem`, `Category`
- [ ] Initialize Expo SQLite database and run schema migrations on app start
- [ ] Create `trips` table (id, destination, destinationLat, destinationLng, destinationPhoto, startDate, endDate, nightLength)
- [ ] Create `pastimes` table (id, name, iconKey)
- [ ] Create `packable_items` table (id, name, categoryId, isPacked, isPackedOut)
- [ ] Create `categories` table (id, name)
- [ ] Create `trip_pastimes` join table
- [ ] Create `pastime_items` join table
- [ ] Implement `PackingList.merge()` — JS `reduce`/`Set` de-duplication of items across selected Pastimes
- [ ] Implement `PackableItem.compareIncludingPacking` sort comparator (unchecked first, checked last)

## State Management (Zustand)

- [ ] `useTripStore` — CRUD for trips, active trip selection
- [ ] `useInventoryStore` — CRUD for master inventory items and categories
- [ ] `usePastimeStore` — CRUD for pastimes and their item associations
- [ ] `useLocationStore` — starred/saved coordinates from Google Maps sync

## Dashboard

- [ ] Display list of existing trips as cards (when trips exist)
- [ ] Trip card shows destination name, dates, and days/nights count
- [ ] Tapping a trip card navigates to Trip Details
- [ ] Show "Passive Proximity Monitoring active" banner when geofencing is running

## Trip Creation Flow

- [ ] Build Trip Creation modal/screen
- [ ] Integrate Google Places API autocomplete for destination input
- [ ] Date pickers for Departure Date and Return Date
- [ ] Auto-calculate `nightLength` from date range
- [ ] Pastime multi-select step (choose which Pastimes to pack for)
- [ ] Run proximity check against `useLocationStore` on trip save
- [ ] Show "Nearby Interest" card in creation flow if a starred location is within the buffer zone
- [ ] Request "Always Allow" location permission during trip creation
- [ ] Request Notifications permission during trip creation
- [ ] Schedule local push notification for `startDate` if nearby location detected
- [ ] Save new trip to SQLite via `useTripStore`

## Trip Details Screen (Parallax View)

- [ ] Build `TripDetailsScreen` with Reanimated parallax scroll header
- [ ] Header occupies 1/3 of screen height with destination photo
- [ ] Fetch destination photo from Google Places Photos API and cache locally
- [ ] Overlay destination name and "X Days / Y Nights" in bottom-right of header
- [ ] Use Sun icon (primary orange) for days count and Moon icon for nights count
- [ ] Parallax effect: header scrolls at 0.5× speed relative to list
- [ ] Render packing list grouped by category
- [ ] Implement per-item check-off (marks `isPacked = true`)
- [ ] Sort: unchecked items at top, checked items at bottom within each category
- [ ] Wire sort comparator to live list updates

## Check-Back (Return Home) Mode

- [ ] Detect when current date equals/exceeds trip `endDate`
- [ ] Flip Trip Details UI into "Return Mode" on that date
- [ ] Filter list to show only items where `isPackedOut = true`
- [ ] Allow user to check items back in (marks `isPackedBack = true`)

## Master Inventory Screen

- [ ] Build `MasterInventoryScreen` with full scrollable checklist
- [ ] Render items grouped by optional category
- [ ] Persistent fast-add input bar pinned to bottom of screen
- [ ] Add item on submit (name → current category or uncategorized)
- [ ] Swipe-to-delete or long-press menu for item removal
- [ ] Add / rename / delete categories

## Pastimes Screen

- [ ] Build `PastimesScreen` listing all pastimes
- [ ] Create / edit / delete a Pastime
- [ ] Replace legacy `R.drawable` icon references with static Lucide icon map
- [ ] Associate Master Inventory items with a Pastime
- [ ] Preview merged item list for a Pastime

## Proximity Alerts & Geofencing

- [ ] Build `ProximityManager.ts` service implementing the Haversine Formula
- [ ] Google Maps OAuth integration (Google People API) to import starred locations
- [ ] Manual import of Google Takeout JSON/KML file as fallback
- [ ] Settings screen with buffer-zone radius slider (5 km – 50 km)
- [ ] Register Expo TaskManager background task for location polling
- [ ] Poll device location ~every 60 minutes (user-configurable) while trip is active
- [ ] Compare current GPS coords against `useLocationStore` starred points
- [ ] Fire real-time notification when user enters buffer zone of a starred location
- [ ] De-register background task automatically when trip `endDate` passes
- [ ] Suggest relevant Pastime when a nearby location matches an activity category

## Visual Identity & Branding

- [ ] Generate launcher icon from `/assets/rucksack-icon.svg` centered on `#FFAD1C` circular background
- [ ] Replace default Expo splash screen with branded version (orange background, white icon)
- [ ] Apply primary orange to active drawer item indicator
- [ ] Apply primary orange to all primary action buttons and active tab indicators
- [ ] Verify Sun/Moon duration overlay in Trip Details uses primary orange

## Navigation & UX Polish

- [ ] Add stack navigator inside the drawer for Trip Details (push from Dashboard)
- [ ] Implement back navigation from Trip Details to Dashboard
- [ ] Add empty state to Master Inventory screen
- [ ] Add empty state to Pastimes screen
- [ ] Handle Android back button correctly within the drawer navigator

## Developer Experience (Non-Critical)

- [ ] Extract inline UI into reusable components (app bar, FAB, empty state, drawer header, placeholder screen)
- [ ] Set up [Storybook for React Native](https://storybook.js.org/tutorials/intro-to-storybook/react-native/en/get-started/) to browse and develop components in isolation
- [ ] Write stories for each component covering key states (e.g. empty state, loaded state, active/inactive variants)
- [ ] Audit and resolve all developer warnings (Reanimated reduced-motion warning, peer dependency mismatches, and any console warnings surfaced in the Metro/debugger output)
