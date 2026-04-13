package com.example.tripswipe

import android.app.Application
import com.example.tripswipe.auth.FirebaseBootstrap

class TripSwipeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseBootstrap.initialize(this)
    }
}
