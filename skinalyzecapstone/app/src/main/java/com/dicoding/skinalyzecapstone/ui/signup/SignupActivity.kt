package com.dicoding.skinalyzecapstone.ui.signup


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.skinalyzecapstone.R
import com.dicoding.skinalyzecapstone.data.UserRepository
import com.dicoding.skinalyzecapstone.data.api.ApiConfig
import com.dicoding.skinalyzecapstone.data.pref.UserPreference
import com.dicoding.skinalyzecapstone.databinding.ActivitySignupBinding
import com.dicoding.skinalyzecapstone.ui.ViewModelFactory
import com.dicoding.skinalyzecapstone.ui.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    val AppCompatActivity.dataStore by preferencesDataStore(name = "user_preferences")
    private lateinit var binding: ActivitySignupBinding
    private val signupViewModel: SignupViewModel by viewModels {
        ViewModelFactory(
            UserRepository(
                UserPreference.getInstance(dataStore), // Pastikan dataStore sudah diinisialisasi di Application
                ApiConfig.getApiService()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        observeViewModel()
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
                    signupViewModel.register(name, email, password)
                }
            }
        }
    }

    private fun observeViewModel() {
        signupViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        signupViewModel.registerResult.observe(this) { result ->
            result.onSuccess { response ->
                Toast.makeText(this, getString(R.string.signup_success), Toast.LENGTH_LONG).show()
                // Setelah berhasil daftar, langsung login
                loginAfterSignup(response.registerResponse[0].email, response.registerResponse[0].password)
            }
            result.onFailure { throwable ->
                Toast.makeText(this, throwable.message, Toast.LENGTH_LONG).show()
            }
        }

        signupViewModel.loginResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_LONG).show()
                // Arahkan ke halaman utama atau login
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            result.onFailure { throwable ->
                Toast.makeText(this, throwable.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loginAfterSignup(email: String, password: String) {
        signupViewModel.login(email, password)
    }
}
