package com.dicoding.skinalyzecapstone.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.skinalyzecapstone.MainActivity
import com.dicoding.skinalyzecapstone.MyEditText
import com.dicoding.skinalyzecapstone.R
import com.dicoding.skinalyzecapstone.data.api.ApiConfig
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupView()
        setupAction()
    }

    private fun setupView() {
        // Mengatur status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    private fun setupAction() {
        val loginButton: Button = findViewById(R.id.my_button)
        val emailInput: TextInputEditText = findViewById(R.id.emailEditText)
        val passwordInput: MyEditText = findViewById(R.id.passwordEditText)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty()) {
                emailInput.error = getString(R.string.email_empty)
            } else if (password.isEmpty()) {
                passwordInput.error = getString(R.string.password_empty)
            } else {
                progressBar.visibility = View.VISIBLE
                performLogin(email, password, progressBar)
            }
        }
    }

    private fun performLogin(email: String, password: String, progressBar: ProgressBar) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiConfig.getApiService().login(email, password)
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (response.token.isNotEmpty()) {
                        Toast.makeText(this@LoginActivity, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@LoginActivity, getString(R.string.login_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
