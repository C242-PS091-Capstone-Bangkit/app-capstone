package com.dicoding.skinalyzecapstone.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.WindowCompat
import com.dicoding.skinalyzecapstone.MainActivity
import com.dicoding.skinalyzecapstone.MyEditText
import com.dicoding.skinalyzecapstone.R
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupView()
        setupAction()
    }

    private fun setupView() {
        // Mengatur status bar agar transparan dan mendukung edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.guidelineHorizontal)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupAction() {
        val loginButton: Button = findViewById(R.id.my_button)
        val emailInput: TextInputEditText = findViewById(R.id.emailEditText)
        val passwordInput: MyEditText = findViewById(R.id.passwordEditText) // Ubah tipe ke MyEditText
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

                login(email, password) { success ->
                    progressBar.visibility = View.GONE
                    if (success) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun login(email: String, password: String, callback: (Boolean) -> Unit) {
        // Contoh login sederhana, ganti dengan API atau validasi sebenarnya
        callback(email == "123" && password == "123")
    }
}

