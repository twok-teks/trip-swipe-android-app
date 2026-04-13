package com.example.tripswipe.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tripswipe.auth.UserSessionStorage
import com.example.tripswipe.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionStorage: UserSessionStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionStorage = UserSessionStorage(this)

        if (sessionStorage.isLoggedIn()) {
            openMainPage()
            return
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text?.toString().orEmpty()
            val password = binding.editTextPassword.text?.toString().orEmpty()
            clearErrors()

            lifecycleScope.launch {
                setLoading(true)
                val error = sessionStorage.login(email, password)
                setLoading(false)
                if (error != null) {
                    if (email.trim().isBlank()) binding.textInputEmail.error = "Required"
                    if (password.trim().isBlank()) binding.textInputPassword.error = "Required"
                    Toast.makeText(this@LoginActivity, error, Toast.LENGTH_SHORT).show()
                } else {
                    openMainPage()
                }
            }
        }

        binding.buttonCreateAccount.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }
    }

    private fun clearErrors() {
        binding.textInputEmail.error = null
        binding.textInputPassword.error = null
    }

    private fun setLoading(isLoading: Boolean) {
        binding.buttonLogin.isEnabled = !isLoading
        binding.buttonCreateAccount.isEnabled = !isLoading
        binding.buttonLogin.text = if (isLoading) "Signing in..." else "Log In"
    }

    private fun openMainPage() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
