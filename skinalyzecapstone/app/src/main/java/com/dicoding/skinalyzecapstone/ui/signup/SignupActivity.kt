package com.dicoding.skinalyzecapstone.ui.signup

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.skinalyzecapstone.R
import com.dicoding.skinalyzecapstone.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.buttonRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            when {
                name.isEmpty() -> {
                    binding.tilName.error = resources.getString(R.string.name_empty)
                }
                email.isEmpty() -> {
                    binding.tilEmail.error = resources.getString(R.string.email_empty)
                }
                password.isEmpty() -> {
                    binding.tilPassword.error = resources.getString(R.string.password_empty)
                }
                else -> {
                    binding.tilName.error = null
                    binding.tilEmail.error = null
                    binding.tilPassword.error = null
                    showToastMessage(name, email)
                }
            }
        }
    }

    private fun showToastMessage(name: String, email: String) {
        Toast.makeText(
            this,
            getString(R.string.signup_success_message, name, email),
            Toast.LENGTH_LONG
        ).show()
    }
}
