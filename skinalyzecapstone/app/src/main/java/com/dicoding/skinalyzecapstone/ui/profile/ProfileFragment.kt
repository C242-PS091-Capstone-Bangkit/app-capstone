package com.dicoding.skinalyzecapstone.ui.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dicoding.skinalyzecapstone.R
import com.dicoding.skinalyzecapstone.data.pref.UserPreference
import com.dicoding.skinalyzecapstone.databinding.FragmentProfileBinding
import com.dicoding.skinalyzecapstone.ui.help.HelpActivity
import kotlinx.coroutines.launch
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editProfileButton.setOnClickListener {
            val intent = Intent(requireContext(), EditProfile::class.java)
            startActivity(intent)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            UserPreference.getInstance(requireContext().dataStore).getSession().collect { user ->
                binding.userName.text = user.name
                binding.userEmail.text = user.email
            }
        }
        // Skinalyze History button click listener
        binding.skinalyzeHistoryButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_history)
        }

        // Daily Reminder button click listener
        binding.dailyReminderButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_notification)
        }

        // Contact Us button click listener
        binding.contactUsButton.setOnClickListener {
            val phoneNumber = "+6282235443630"
            val url = "https://wa.me/$phoneNumber"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        // Help button click listener
        binding.helpButton.setOnClickListener {
            val intent = Intent(requireContext(), HelpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
