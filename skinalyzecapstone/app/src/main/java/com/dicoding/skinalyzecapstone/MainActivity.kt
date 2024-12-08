package com.dicoding.skinalyzecapstone

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dicoding.skinalyzecapstone.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get NavController from NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        // Setup BottomNavigationView with NavController
        val navView: BottomNavigationView = binding.bottomNavigation
        navView.setupWithNavController(navController)

        // Set click listener for FloatingActionButton to navigate to ScanFragment
        binding.fab.setOnClickListener {
            Log.d("FAB_Click", "FAB clicked, navigating to ScanFragment")
            val options = NavOptions.Builder()
                .setPopUpTo(R.id.navigation_home, true)
                .build()
            navController.navigate(R.id.navigation_scan, null, options)
        }

        // Handle bottom navigation click events
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    Log.d("Navigation", "Navigating to HomeFragment")
                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_notification -> {
                    Log.d("Navigation", "Navigating to NotificationFragment")
                    navController.navigate(R.id.navigation_notification)
                    true
                }
                R.id.navigation_profile -> {
                    Log.d("Navigation", "Navigating to ProfileFragment")
                    navController.navigate(R.id.navigation_profile)
                    true
                }
                R.id.navigation_history -> {
                    Log.d("Navigation", "Navigating to HistoryFragment")
                    navController.navigate(R.id.navigation_history)
                    true
                }
                else -> false
            }
        }
    }
}
