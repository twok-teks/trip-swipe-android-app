package com.example.tripswipe.auth

import android.content.Context
import com.example.tripswipe.AppConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

object FirebaseBootstrap {
    fun initialize(context: Context): Boolean {
        if (FirebaseApp.getApps(context).isNotEmpty()) return true
        if (!AppConfig.isFirebaseConfigured) return false

        val optionsBuilder = FirebaseOptions.Builder()
            .setApiKey(AppConfig.firebaseApiKey)
            .setApplicationId(AppConfig.firebaseAppId)
            .setProjectId(AppConfig.firebaseProjectId)

        if (AppConfig.firebaseStorageBucket.isNotBlank()) {
            optionsBuilder.setStorageBucket(AppConfig.firebaseStorageBucket)
        }

        FirebaseApp.initializeApp(context, optionsBuilder.build())
        return true
    }
}
