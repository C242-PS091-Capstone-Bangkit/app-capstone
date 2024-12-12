package com.dicoding.skinalyzecapstone.ui.history

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
import com.dicoding.skinalyzecapstone.databinding.FragmentHistoryBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var userRepository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        // Inisialisasi UserRepository
        val dataStore = requireContext().dataStore
        userRepository = UserRepository.getInstance(
            UserPreference.getInstance(dataStore),
            ApiConfig.getApiServiceGeneral()
        )

        // Ambil ID user yang sedang login
        viewLifecycleOwner.lifecycleScope.launch {
            val userId = try {
                UserPreference.getInstance(dataStore).getSession().first().idUser.toInt()
            } catch (e: NumberFormatException) {
                Log.e("HistoryFragment", "Error converting user ID to Int: ${e.message}")
                0 // Atau nilai default lainnya jika konversi gagal
            }

            // Panggil API untuk mengambil history berdasarkan ID user
            try {
                val historyResponse = userRepository.getHistoryById(userId) // historyResponse sekarang bertipe List

                val historyAdapter = HistoryAdapter(historyResponse)
                binding.rvHistory.adapter = historyAdapter
                Log.d("HistoryFragment", "History response: $historyResponse")
            } catch (e: Exception) {
                Log.e("HistoryFragment", "Error getting history: ${e.message}", e)
                // Tangani error (misalnya, tampilkan pesan error)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}