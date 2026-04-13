package com.example.tripswipe.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tripswipe.auth.UserSessionStorage
import com.example.tripswipe.databinding.ActivityCreateAccountBinding
import kotlinx.coroutines.launch

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var sessionStorage: UserSessionStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionStorage = UserSessionStorage(this)

        binding.buttonBackToLogin.setOnClickListener { finish() }

        binding.buttonCreateAccount.setOnClickListener {
            val email = binding.editTextEmail.text?.toString().orEmpty()
            val password = binding.editTextPassword.text?.toString().orEmpty()
            val verifyPassword = binding.editTextVerifyPassword.text?.toString().orEmpty()

            clearErrors()

            lifecycleScope.launch {
                setLoading(true)
                val error = sessionStorage.createAccount(email, password, verifyPassword)
                setLoading(false)
                if (error != null) {
                    if (email.trim().isBlank()) binding.textInputEmail.error = "Required"
                    if (password.trim().isBlank()) binding.textInputPassword.error = "Required"
                    if (verifyPassword.trim().isBlank()) binding.textInputVerifyPassword.error = "Required"
                    if (password.isNotBlank() && verifyPassword.isNotBlank() && password.trim() != verifyPassword.trim()) {
                        binding.textInputVerifyPassword.error = "Does not match"
                    }
                    Toast.makeText(this@CreateAccountActivity, error, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CreateAccountActivity, "Account created successfully.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@CreateAccountActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun clearErrors() {
        binding.textInputEmail.error = null
        binding.textInputPassword.error = null
        binding.textInputVerifyPassword.error = null
    }

    private fun setLoading(isLoading: Boolean) {
        binding.buttonCreateAccount.isEnabled = !isLoading
        binding.buttonBackToLogin.isEnabled = !isLoading
        binding.buttonCreateAccount.text = if (isLoading) "Creating..." else "Create Account"
    }
}
