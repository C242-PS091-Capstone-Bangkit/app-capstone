package com.dicoding.skinalyzecapstone.ui.profile

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.dicoding.skinalyzecapstone.R
import com.dicoding.skinalyzecapstone.data.UserRepository
import com.dicoding.skinalyzecapstone.data.pref.UserPreference
import com.dicoding.skinalyzecapstone.data.api.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class EditUsernameActivity : AppCompatActivity() {

    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_username)

        val rootView = findViewById<ScrollView>(R.id.scrollView)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UserRepository
        val dataStore = applicationContext.dataStore
        userRepository = UserRepository.getInstance(
            UserPreference.getInstance(dataStore),
            ApiConfig.getApiServiceGeneral()
        )

        val usernameEditText = findViewById<EditText>(R.id.editTextUsername)
        val saveButton = findViewById<Button>(R.id.saveButton)

        // Set up Save Button
        saveButton.setOnClickListener {
            val newUsername = usernameEditText.text.toString().trim()

            if (newUsername.isEmpty()) {
                Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            updateUsername(newUsername)
        }
    }

    private fun updateUsername(newUsername: String) {
        lifecycleScope.launch {
            val userId = try {
                UserPreference.getInstance(applicationContext.dataStore).getSession().first().idUser.toInt()
            } catch (e: NumberFormatException) {
                Toast.makeText(this@EditUsernameActivity, "Invalid user ID", Toast.LENGTH_SHORT).show()
                return@launch
            }

            try {
                // Fetch user data to get email and password
                val currentUser = userRepository.getUserById(userId)

                // Validate data
                val email = currentUser.email
                val password = currentUser.password

                if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EditUsernameActivity, "Email or password is missing", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                // Call API to update username
                val response = userRepository.editUser(
                    id = userId,
                    username = newUsername,
                    email = email,
                )

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EditUsernameActivity, response.message, Toast.LENGTH_SHORT).show()
                    finish() // Close activity after successful update
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EditUsernameActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
