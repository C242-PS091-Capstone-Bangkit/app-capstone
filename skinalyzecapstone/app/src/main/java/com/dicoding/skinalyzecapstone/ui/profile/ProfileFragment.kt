package com.dicoding.skinalyzecapstone.ui.profile

import androidx.fragment.app.Fragment
import com.dicoding.skinalyzecapstone.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}