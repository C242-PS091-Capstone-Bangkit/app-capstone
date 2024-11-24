package com.dicoding.skinalyzecapstone.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.skinalyzecapstone.databinding.ItemReminderBinding
import com.dicoding.skinalyzecapstone.ui.model.Reminder

class ReminderAdapter(private val reminderList: List<Reminder>) :
    RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    inner class ReminderViewHolder(private val binding: ItemReminderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(reminder: Reminder) {
            binding.reminderImage.setImageResource(reminder.imageResId)
            binding.reminderText.text = reminder.text
            binding.reminderSwitch.isChecked = reminder.isEnabled
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val binding = ItemReminderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReminderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(reminderList[position])
    }

    override fun getItemCount(): Int = reminderList.size
}
