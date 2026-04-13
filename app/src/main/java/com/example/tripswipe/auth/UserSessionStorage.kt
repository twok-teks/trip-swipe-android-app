package com.example.tripswipe.auth

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class UserSessionStorage(context: Context) {
    private val appContext = context.applicationContext

    fun isConfigured(): Boolean = firebaseAuthOrNull() != null

    fun isLoggedIn(): Boolean = firebaseAuthOrNull()?.currentUser != null

    fun getCurrentUsername(): String {
        val user = firebaseAuthOrNull()?.currentUser ?: return ""
        return user.email?.trim().orEmpty().ifBlank { user.uid }
    }

    fun getCurrentUserKey(): String {
        val user = firebaseAuthOrNull()?.currentUser ?: return "guest"
        return user.uid.trim().ifBlank { user.email?.trim().orEmpty() }.ifBlank { "guest" }
    }

    fun getDisplayName(): String {
        val user = firebaseAuthOrNull()?.currentUser ?: return "Traveler"
        return user.displayName?.trim().orEmpty()
            .ifBlank { user.email?.substringBefore("@").orEmpty() }
            .ifBlank { "Traveler" }
    }

    fun getProfileSubtitle(): String {
        val user = firebaseAuthOrNull()?.currentUser ?: return "Sign in with your email and password to sync your TripSwipe profile."
        return user.email?.trim().orEmpty().ifBlank { "Signed in to TripSwipe" }
    }

    suspend fun login(email: String, password: String): String? {
        val auth = firebaseAuthOrNull()
            ?: return "Firebase is not configured yet. Add the Firebase values to local.properties first."
        val cleanedEmail = email.trim()
        val cleanedPassword = password.trim()

        if (cleanedEmail.isBlank() || cleanedPassword.isBlank()) {
            return "Enter both email and password."
        }

        return try {
            auth.signInWithEmailAndPassword(cleanedEmail, cleanedPassword).await()
            null
        } catch (error: Exception) {
            error.message ?: "Firebase sign-in failed."
        }
    }

    suspend fun createAccount(
        email: String,
        password: String,
        verifyPassword: String
    ): String? {
        val auth = firebaseAuthOrNull()
            ?: return "Firebase is not configured yet. Add the Firebase values to local.properties first."
        val cleanedEmail = email.trim()
        val cleanedPassword = password.trim()
        val cleanedVerifyPassword = verifyPassword.trim()

        if (cleanedEmail.isBlank() || cleanedPassword.isBlank() || cleanedVerifyPassword.isBlank()) {
            return "Please fill in every field."
        }
        if (cleanedPassword.length < 6) {
            return "Password must be at least 6 characters."
        }
        if (cleanedPassword != cleanedVerifyPassword) {
            return "Passwords do not match."
        }

        return try {
            auth.createUserWithEmailAndPassword(cleanedEmail, cleanedPassword).await()
            null
        } catch (error: Exception) {
            error.message ?: "Firebase account creation failed."
        }
    }

    fun logout() {
        firebaseAuthOrNull()?.signOut()
    }

    private fun firebaseAuthOrNull(): FirebaseAuth? {
        if (!FirebaseBootstrap.initialize(appContext)) return null
        return FirebaseAuth.getInstance()
    }
}
