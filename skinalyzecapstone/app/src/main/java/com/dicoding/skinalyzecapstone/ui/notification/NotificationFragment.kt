package com.dicoding.skinalyzecapstone.ui.notification

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.skinalyzecapstone.data.UserRepository
import com.dicoding.skinalyzecapstone.data.api.ApiConfig
import com.dicoding.skinalyzecapstone.data.pref.UserPreference
import com.dicoding.skinalyzecapstone.data.response.ReminderResponse
import com.dicoding.skinalyzecapstone.databinding.FragmentNotificationBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private lateinit var userRepository: UserRepository
    private lateinit var notificationAdapter: NotificationAdapter
    private val reminderList = mutableListOf<ReminderResponse>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        binding.rvReminder.layoutManager = LinearLayoutManager(requireContext())

        // Inisialisasi UserRepository
        val dataStore = requireContext().dataStore
        userRepository = UserRepository.getInstance(
            UserPreference.getInstance(dataStore),
            ApiConfig.getApiServiceGeneral()
        )

        // Ambil data reminders
        fetchReminders(dataStore)
    }

    private fun fetchReminders(dataStore: DataStore<Preferences>) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Ambil ID user yang sedang login
                val userId = UserPreference.getInstance(dataStore).getSession().first().idUser.toInt()

                // Panggil API untuk mendapatkan semua reminders
                val allReminders = userRepository.getRemindersByUserId(userId)

                // Filter hanya reminders yang cocok dengan id_user
                reminderList.clear()
                reminderList.addAll(allReminders.filter { it.id == userId }) // Pastikan filter berdasarkan id_user

                if (reminderList.isNotEmpty()) {
                    notificationAdapter = NotificationAdapter(reminderList, ::onDeleteClick)
                    binding.rvReminder.adapter = notificationAdapter
                } else {
                    Log.d("NotificationFragment", "No reminders found for user ID: $userId")
                    Toast.makeText(requireContext(), "No reminders found", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("NotificationFragment", "Error fetching reminders: ${e.message}", e)
                Toast.makeText(requireContext(), "Error loading reminders", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onDeleteClick(reminder: ReminderResponse) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Panggil API untuk menghapus reminder
                val response = userRepository.deleteReminder(reminder.idReminder ?: 0)

                // Validasi respons API
                if (!response.message.isNullOrEmpty() && response.message.contains("deleted", ignoreCase = true)) {
                    // Hapus reminder dari daftar
                    reminderList.remove(reminder)
                    notificationAdapter.notifyDataSetChanged()
                    Toast.makeText(requireContext(), "Reminder deleted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Reminder deleted successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("NotificationFragment", "Error deleting reminder: ${e.message}", e)
                Toast.makeText(requireContext(), "Error deleting reminder", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
