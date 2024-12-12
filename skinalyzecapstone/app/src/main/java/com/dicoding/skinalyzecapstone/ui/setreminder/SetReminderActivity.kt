package com.dicoding.skinalyzecapstone.ui.setreminder

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.skinalyzecapstone.R
import com.dicoding.skinalyzecapstone.data.UserRepository
import com.dicoding.skinalyzecapstone.data.api.ApiConfig
import com.dicoding.skinalyzecapstone.data.pref.UserPreference
import com.dicoding.skinalyzecapstone.data.response.CreateReminderResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

// DataStore Ekstensi
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.skinalyzecapstone.MainActivity

private val android.content.Context.dataStore by preferencesDataStore("user_preferences")

class SetReminderActivity : AppCompatActivity() {

    private lateinit var reminderTitle: EditText
    private lateinit var reminderDescription: EditText
    private lateinit var remindTimeInput: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var backButton: ImageView
    private lateinit var userRepository: UserRepository

    private var idUser: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        // Inisialisasi Views
        reminderTitle = findViewById(R.id.reminderTitle)
        reminderDescription = findViewById(R.id.reminderDescription)
        remindTimeInput = findViewById(R.id.remindTimeInput)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)
        backButton = findViewById(R.id.backButton)

        // Inisialisasi UserRepository
        val userPreference = UserPreference.getInstance(dataStore)
        userRepository = UserRepository.getInstance(userPreference, ApiConfig.getApiServiceGeneral())

        // Ambil ID Pengguna dari DataStore
        lifecycleScope.launch {
            try {
                idUser = userPreference.getSession().first().idUser.toInt()
                Log.d("SetReminderActivity", "User ID: $idUser")
            } catch (e: NumberFormatException) {
                Log.e("SetReminderActivity", "Error parsing user ID: ${e.message}")
                idUser = 0 // Default jika gagal parsing
            }
        }

        // Buka Time Picker saat klik kolom waktu
        remindTimeInput.setOnClickListener { showTimePicker() }

        // Simpan Data
        saveButton.setOnClickListener {
            val title = reminderTitle.text.toString().trim()
            val description = reminderDescription.text.toString().trim()
            val time = remindTimeInput.text.toString().trim()

            if (title.isEmpty() || description.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields!", Toast.LENGTH_SHORT).show()
            } else {
                createReminder(title, description, time)
            }
        }

        // Hapus Data
        deleteButton.setOnClickListener {
            reminderTitle.text.clear()
            reminderDescription.text.clear()
            remindTimeInput.text.clear()
        }

        // Kembali ke Halaman Sebelumnya
        backButton.setOnClickListener { finish() }
    }

    // TimePickerDialog
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val time = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                remindTimeInput.setText(time)
            },
            hour,
            minute,
            true
        ).show()
    }

    // Kirim Data ke API
    private fun createReminder(title: String, description: String, time: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.d("SetReminderActivity", "Kirim ke API: idUser=$idUser, title=$title, description=$description, time=$time")

                val response = ApiConfig.getApiServiceGeneral().createReminder(idUser, title, description, time)

                Log.d("SetReminderActivity", "Respons API: ${response.message}")

                withContext(Dispatchers.Main) {
                    handleCreateReminderResponse(response)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SetReminderActivity, "Failed to save reminder", Toast.LENGTH_SHORT).show()
                    Log.e("SetReminderActivity", "Error: ${e.message}")
                }
            }
        }
    }

    // Tampilkan Respons API
    private fun handleCreateReminderResponse(response: CreateReminderResponse) {
        Log.d("SetReminderActivity", "Respons: ${response.message}")
        Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()

        // Pindah ke MainActivity (yang menampilkan NotificationFragment)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent) // Mulai MainActivity
        finish() // Tutup SetReminderActivity
    }
}
