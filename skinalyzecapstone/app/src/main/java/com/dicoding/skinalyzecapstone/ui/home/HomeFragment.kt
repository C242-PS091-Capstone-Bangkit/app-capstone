package com.dicoding.skinalyzecapstone.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.skinalyzecapstone.R
import com.dicoding.skinalyzecapstone.databinding.FragmentHomeBinding
import com.dicoding.skinalyzecapstone.ui.adapter.ReminderAdapter
import com.dicoding.skinalyzecapstone.ui.model.Reminder

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupReminderList()

        return root
    }

    private fun setupReminderList() {
        val reminderList = listOf(
            Reminder("Notification 1", R.drawable.skincare, true),
            Reminder("Notification 2", R.drawable.skincare, false),
            Reminder("Notification 3", R.drawable.skincare, true),
            Reminder("Notification 4", R.drawable.skincare, false),
            Reminder("Notification 5", R.drawable.skincare, true),
            Reminder("Notification 6", R.drawable.skincare, false)
        )

        val adapter = ReminderAdapter(reminderList)
        binding.reminderList.layoutManager = GridLayoutManager(context, 2) // 2 kolom
        binding.reminderList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
