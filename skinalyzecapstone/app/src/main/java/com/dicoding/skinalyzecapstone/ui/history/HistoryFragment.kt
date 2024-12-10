package com.dicoding.skinalyzecapstone.ui.history

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.skinalyzecapstone.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchBar()

        // Populate with dummy data
        val historyList = fetchHistoryData()

        if (historyList.isNotEmpty()) {
            showHistoryList(historyList)
        } else {
            showEmptyPlaceholder()
        }
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter()
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecyclerView.adapter = adapter
    }

    private fun setupSearchBar() {
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterHistory(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showHistoryList(historyList: List<HistoryAdapter.HistoryItem>) {
        Log.d("HistoryFragment", "Displaying history list: ${historyList.size} items")
        binding.emptyPlaceholder.visibility = View.GONE
        binding.historyRecyclerView.visibility = View.VISIBLE
        adapter.submitList(historyList)
    }


    private fun showEmptyPlaceholder() {
        binding.emptyPlaceholder.visibility = View.VISIBLE
        binding.historyRecyclerView.visibility = View.GONE
    }

    private fun filterHistory(query: String) {
        val filteredList = fetchHistoryData().filter {
            it.title.contains(query, ignoreCase = true)
        }

        if (filteredList.isNotEmpty()) {
            showHistoryList(filteredList)
        } else {
            showEmptyPlaceholder()
        }
    }

    private fun fetchHistoryData(): List<HistoryAdapter.HistoryItem> {
        return listOf(
            HistoryAdapter.HistoryItem("Skincare Session 1", "2024-12-01"),
            HistoryAdapter.HistoryItem("Skincare Session 2", "2024-12-02"),
            HistoryAdapter.HistoryItem("Skincare Session 3", "2024-12-03"),
            HistoryAdapter.HistoryItem("Skincare Session 4", "2024-12-04"),
            HistoryAdapter.HistoryItem("Skincare Session 5", "2024-12-05")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
