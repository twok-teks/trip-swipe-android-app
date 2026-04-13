# TripSwipe Android App

TripSwipe is a pastel-themed Android app with built-in travel data for three cities:
- Los Angeles
- Miami
- New York

## Main flow
1. Choose a city from the home screen.
2. Swipe left or right on attraction cards.
3. Finish early or swipe through all cards.
4. View your saved attractions.
5. Filter by category such as Museum, Park, Restaurant, or Amusement.
6. Open the attraction website or Google Maps using links stored in the app.

## Major Android functionalities used
- RecyclerView
- Swipe gestures with ItemTouchHelper
- Multiple activities and intent navigation
- Implicit intents for browser and Google Maps
- Firebase Authentication with email/password sign-in
- Remote image loading with local fallbacks

## Notes
- No database is used.
- Attraction metadata and fallback images are stored locally in `TripRepository.kt`.
- Remote attraction and city photos are prepared through Google Places image lookups when `PLACES_API_KEY` is provided in `local.properties`.
- Firebase Authentication is initialized from `local.properties` values so the project can compile before credentials are added.
- The app uses a minimalist pastel design with rounded cards and a script-like `cursive` font family approximation.

## local.properties setup
Add these values to `local.properties` before testing Firebase email/password auth and remote Places images:

```properties
FIREBASE_API_KEY=
FIREBASE_APP_ID=
FIREBASE_PROJECT_ID=
FIREBASE_STORAGE_BUCKET=
PLACES_API_KEY=
```

## Open in Android Studio
Open the `tripswipe_app` folder in Android Studio and let Gradle sync.
