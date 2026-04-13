package com.example.tripswipe.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tripswipe.auth.UserSessionStorage
import com.example.tripswipe.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var sessionStorage: UserSessionStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionStorage = UserSessionStorage(this)

        if (!sessionStorage.isLoggedIn()) {
            openLoginPage()
            return
        }

        binding.textProfileName.text = sessionStorage.getDisplayName()
        binding.textProfileSubtitle.text = sessionStorage.getProfileSubtitle()

        binding.buttonBack.setOnClickListener { finish() }
        binding.buttonLogout.setOnClickListener {
            sessionStorage.logout()
            openLoginPage()
        }
    }

    private fun openLoginPage() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
