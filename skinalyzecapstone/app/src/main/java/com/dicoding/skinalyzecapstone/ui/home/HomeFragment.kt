package com.dicoding.skinalyzecapstone.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.skinalyzecapstone.R
import com.dicoding.skinalyzecapstone.databinding.FragmentHomeBinding
import com.dicoding.skinalyzecapstone.ui.adapter.ReminderAdapter
import com.dicoding.skinalyzecapstone.data.UserRepository
import com.dicoding.skinalyzecapstone.data.api.ApiConfig
import com.dicoding.skinalyzecapstone.data.pref.UserPreference
import com.dicoding.skinalyzecapstone.data.response.ReminderResponse
import com.dicoding.skinalyzecapstone.ui.model.Reminder
import com.dicoding.skinalyzecapstone.ui.setreminder.SetReminderActivity
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    val Context.dataStore by preferencesDataStore(name = "user_preferences")

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var userRepository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        userRepository = UserRepository.getInstance(
            UserPreference.getInstance(requireContext().dataStore),
            ApiConfig.getApiServiceGeneral()
        )

        // Set action for the "Add Reminder" button
        binding.buttonSetReminder.setOnClickListener {
            startActivity(Intent(context, SetReminderActivity::class.java))
        }

        loadUserSession()
        return binding.root
    }

    private fun loadUserSession() {
        lifecycleScope.launch {
            userRepository.getUserSession().collect { user ->
                binding.textHiUser.text = "Hi, ${user.name}!"
                loadReminders(user.idUser.toInt())
            }
        }
    }

    private fun loadReminders(userId: Int) {
        lifecycleScope.launch {
            try {
                val reminders = userRepository.getRemindersByUserId(userId)
                if (reminders.isEmpty()) {
                    showReminderNotFound()
                } else {
                    setupRecyclerView(reminders)
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to load reminders", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView(reminders: List<ReminderResponse>) {
        val reminderList = reminders.mapNotNull {
            it.judulReminder?.let { title -> Reminder(title, R.drawable.skincare, true) }
        }

        val adapter = ReminderAdapter(reminderList)
        binding.reminderList.layoutManager = GridLayoutManager(context, 2)
        binding.reminderList.adapter = adapter
    }

    private fun showReminderNotFound() {
        binding.reminderList.visibility = View.GONE
        binding.textNoReminder.visibility = View.VISIBLE
        binding.buttonSetReminder.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}