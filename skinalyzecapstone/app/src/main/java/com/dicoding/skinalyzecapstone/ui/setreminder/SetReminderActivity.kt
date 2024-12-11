package com.dicoding.skinalyzecapstone.ui.setreminder

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.skinalyzecapstone.R
import java.util.*

class SetReminderActivity : AppCompatActivity() {

    private lateinit var reminderTitle: EditText
    private lateinit var reminderDescription: EditText
    private lateinit var remindTimeInput: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        // Initialize Views
        reminderTitle = findViewById(R.id.reminderTitle)
        reminderDescription = findViewById(R.id.reminderDescription)
        remindTimeInput = findViewById(R.id.remindTimeInput)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)
        backButton = findViewById(R.id.backButton)

        // Open Time Picker on clicking Time Input
        remindTimeInput.setOnClickListener {
            showTimePicker()
        }

        // Handle Save Button Click
        saveButton.setOnClickListener {
            val title = reminderTitle.text.toString()
            val description = reminderDescription.text.toString()
            val time = remindTimeInput.text.toString()

            if (title.isEmpty() || description.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Reminder Saved!\nTitle: $title\nTime: $time", Toast.LENGTH_LONG).show()
                // Save logic here
            }
        }

        // Handle Delete Button Click
        deleteButton.setOnClickListener {
            reminderTitle.text.clear()
            reminderDescription.text.clear()
            remindTimeInput.text.clear()
        }

        // Handle Back Button Click
        backButton.setOnClickListener {
            finish()
        }
    }

    // Show TimePickerDialog
    private fun showTimePicker() {
        val hour = 12
        val minute = 0

        TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val time = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                remindTimeInput.setText(time)
            },
            hour,
            minute,
            true // Use 24-hour format
        ).show()
    }
}
