package com.dicoding.skinalyzecapstone.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.skinalyzecapstone.R
import com.dicoding.skinalyzecapstone.databinding.FragmentHomeBinding
import com.dicoding.skinalyzecapstone.ui.adapter.ImageAdapter
import com.dicoding.skinalyzecapstone.ui.adapter.ImageSliderAdapter
import com.dicoding.skinalyzecapstone.ui.animate.SlowPageTransformer
import com.dicoding.skinalyzecapstone.data.UserRepository
import com.dicoding.skinalyzecapstone.data.api.ApiConfig
import com.dicoding.skinalyzecapstone.data.pref.UserPreference
import com.dicoding.skinalyzecapstone.ui.setreminder.SetReminderActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    val Context.dataStore by preferencesDataStore(name = "user_preferences")

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var userRepository: UserRepository
    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private val imageList = listOf(
        R.drawable.slider1,
        R.drawable.slider2,
    )

    // Daftar gambar skincare
    private val skincareImages = listOf(
        R.drawable.skincare1,
        R.drawable.skincare2,
        R.drawable.skincare3,
        R.drawable.skincare4
    )

    // Daftar judul skincare
    private val skincareTitles = listOf(
        "Dermalogica AGE Bright Clearing Serum 30ml",
        "PIXI Overnight Glow Serum",
        "Aurelia Skincare Firm & Replenish Body Serum 250ml",
        "The Ordinary Vitamin C Suspension Cream 30% in Silicone 30ml"
    )

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

        // Configure the Image Slider
        setupImageSlider()

        // Configure the custom Toolbar
        val toolbar = binding.toolbarHome
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)

        // Configure RecyclerView for skincare images
        setupRecyclerView()

        // Set action for the "Add Reminder" button
        binding.buttonSetReminder.setOnClickListener {
            startActivity(Intent(context, SetReminderActivity::class.java))
        }

        loadUserSession()
        return binding.root
    }

    private fun setupImageSlider() {
        // Inisialisasi adapter dengan daftar gambar
        imageSliderAdapter = ImageSliderAdapter(imageList)
        binding.imageSlider.adapter = imageSliderAdapter
        binding.imageSlider.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // Atur PageTransformer untuk animasi transisi
        binding.imageSlider.setPageTransformer(SlowPageTransformer())

        // Automatic slide setiap 4 detik
        lifecycleScope.launchWhenResumed {
            while (isActive) {
                delay(4000) // Tunggu 4 detik
                val nextItem = (binding.imageSlider.currentItem + 1) % imageList.size
                binding.imageSlider.setCurrentItem(nextItem, true) // Berpindah dengan animasi
            }
        }
    }

    private fun setupRecyclerView() {
        // Membuat adapter untuk RecyclerView
        val skincareAdapter = ImageAdapter(skincareImages, skincareTitles)
        binding.imageRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2) // Tampilkan dalam grid 2 kolom
            adapter = skincareAdapter
        }
    }

    private fun loadUserSession() {
        lifecycleScope.launch {
            userRepository.getUserSession().collect { user ->
                binding.textHiUser.text = "Hi, ${user.name}!"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
