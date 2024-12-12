package com.dicoding.skinalyzecapstone.ui.notification

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.skinalyzecapstone.data.UserRepository
import com.dicoding.skinalyzecapstone.data.api.ApiConfig
import com.dicoding.skinalyzecapstone.data.pref.UserPreference
import com.dicoding.skinalyzecapstone.databinding.FragmentNotificationBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private lateinit var userRepository: UserRepository

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

        binding.rvReminder.layoutManager = LinearLayoutManager(requireContext())

        // Inisialisasi UserRepository
        val dataStore = requireContext().dataStore
        userRepository = UserRepository.getInstance(
            UserPreference.getInstance(dataStore),
            ApiConfig.getApiServiceGeneral()
        )

        // Ambil ID user yang sedang login
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val userId = UserPreference.getInstance(dataStore).getSession().first().idUser.toInt()

                // Panggil API untuk mendapatkan semua reminder
                val allReminders = userRepository.getRemindersByUserId(userId)

                // Filter hanya reminder yang cocok dengan id_user
                val filteredReminders = allReminders.filter { it.id == userId }

                if (filteredReminders.isNotEmpty()) {
                    // Atur adapter untuk RecyclerView
                    val notificationAdapter = NotificationAdapter(filteredReminders)
                    binding.rvReminder.adapter = notificationAdapter
                } else {
                    // Logika jika data kosong
                    Log.d("NotificationFragment", "No reminders found for user ID: $userId")
                }
            } catch (e: NumberFormatException) {
                Log.e("NotificationFragment", "Invalid user ID format: ${e.message}")
            } catch (e: Exception) {
                Log.e("NotificationFragment", "Error fetching reminders: ${e.message}", e)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


