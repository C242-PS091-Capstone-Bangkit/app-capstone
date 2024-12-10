package com.dicoding.skinalyzecapstone.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.LinearLayout
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.dicoding.skinalyzecapstone.R
import com.dicoding.skinalyzecapstone.data.pref.UserPreference
import com.dicoding.skinalyzecapstone.ui.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class EditProfile : AppCompatActivity() {

    // Mendapatkan DataStore dari Context
    private val Context.dataStore by preferencesDataStore(name = "user_prefs")

    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        // Inisialisasi UserPreference dengan DataStore
        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editUsernameButton: LinearLayout = findViewById(R.id.editUsernameButton)
        val logoutButton: LinearLayout = findViewById(R.id.logoutButton)

        // Tombol Edit Username
        editUsernameButton.setOnClickListener {
            val intent = Intent(this, EditUsername::class.java)
            startActivity(intent)
        }

        // Tombol Logout
        logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        // Membuat AlertDialog konfirmasi logout
        val dialog = AlertDialog.Builder(this)
            .setTitle("Konfirmasi Logout")
            .setMessage("Anda yakin ingin Logout?")
            .setPositiveButton("Ya") { _, _ ->
                logoutUser()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun logoutUser() {
        lifecycleScope.launch {
            // Clear user session from DataStore
            userPreference.clearSession()

            // Redirect to Welcome Activity
            val intent = Intent(this@EditProfile, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
