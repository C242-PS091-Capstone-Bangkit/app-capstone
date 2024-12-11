package com.dicoding.skinalyzecapstone.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.dicoding.skinalyzecapstone.MainActivity
import com.dicoding.skinalyzecapstone.MyEditText
import com.dicoding.skinalyzecapstone.R
import com.dicoding.skinalyzecapstone.data.UserRepository
import com.dicoding.skinalyzecapstone.data.api.ApiConfig
import com.dicoding.skinalyzecapstone.data.pref.UserModel
import com.dicoding.skinalyzecapstone.data.pref.UserPreference
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class LoginActivity : AppCompatActivity() {

    private fun logUserDetails(email: String, username: String?) {
        // Menampilkan informasi pengguna di Logcat
        android.util.Log.d("UserDetails", "Email: $email, Username: ${username ?: "Tidak tersedia"}")
    }

    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi UserRepository
        val userPreference = UserPreference.getInstance(dataStore)
        val apiService = ApiConfig.getApiService()
        userRepository = UserRepository.getInstance(userPreference, apiService)

        setupView()
        setupAction()
    }

    private fun setupView() {
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
        lifecycleScope.launch {
            try {
                val response = userRepository.loginUser(email, password)

                if (response.token.isNotEmpty()) {
                    val user = UserModel(
                        idUser = response.user.id?.toString() ?: "0",
                        name = response.user.username,
                        email = response.user.email,
                        token = response.token,
                        isLogin = true
                    )
                    userRepository.saveUserSession(user)

                    // Panggilan log user
                    logUserDetails(response.user.email, response.user.username)

                    progressBar.visibility = View.GONE
                    Toast.makeText(this@LoginActivity, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                else {
                    showError(progressBar, getString(R.string.login_failed))
                }
            } catch (e: Exception) {
                showError(progressBar, getString(R.string.login_error))
            }
        }
    }

    private fun showError(progressBar: ProgressBar, message: String) {
        progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
