package com.dicoding.skinalyzecapstone.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.skinalyzecapstone.data.response.ReminderResponse
import com.dicoding.skinalyzecapstone.databinding.ItemRemindNotifBinding

class NotificationAdapter(private val reminders: List<ReminderResponse>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(val binding: ItemRemindNotifBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemRemindNotifBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val reminder = reminders[position]
        with(holder.binding) {
            tvReminderTitle.text = reminder.judulReminder ?: "Untitled Reminder"
            tvReminderDescription.text = reminder.deskripsi ?: "No description available"
            tvReminderTime.text = reminder.jamReminder ?: "Unknown Time"
        }
    }

    override fun getItemCount(): Int = reminders.size
}
