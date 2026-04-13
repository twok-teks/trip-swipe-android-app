package com.example.tripswipe

object AppConfig {
    val firebaseApiKey: String = BuildConfig.FIREBASE_API_KEY.trim()
    val firebaseAppId: String = BuildConfig.FIREBASE_APP_ID.trim()
    val firebaseProjectId: String = BuildConfig.FIREBASE_PROJECT_ID.trim()
    val firebaseStorageBucket: String = BuildConfig.FIREBASE_STORAGE_BUCKET.trim()
    val placesApiKey: String = BuildConfig.PLACES_API_KEY.trim()

    val isFirebaseConfigured: Boolean
        get() = firebaseApiKey.isNotBlank() &&
            firebaseAppId.isNotBlank() &&
            firebaseProjectId.isNotBlank()

    val isPlacesConfigured: Boolean
        get() = placesApiKey.isNotBlank()
}
