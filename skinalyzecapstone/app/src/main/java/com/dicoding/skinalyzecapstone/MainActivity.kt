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

        // Track the last selected item on BottomNavigationView
        // Track the last selected item on BottomNavigationView
        var lastSelectedItemId = R.id.navigation_home // Default to home

// Disable BottomNavigationView's default check behavior for ScanFragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_scan) {
                // Clear the selection of all menu items when navigating to ScanFragment
                for (i in 0 until navView.menu.size()) {
                    navView.menu.getItem(i).isChecked = false
                }
            } else {
                // Restore the check state of the last selected item for other destinations
                navView.menu.findItem(lastSelectedItemId)?.isChecked = true
            }
        }

// Set click listener for FloatingActionButton to navigate to ScanFragment
        binding.fab.setOnClickListener {
            Log.d("FAB_Click", "FAB clicked, navigating to ScanFragment")

            // Navigate to ScanFragment
            val options = NavOptions.Builder()
                .setPopUpTo(R.id.navigation_home, true) // Ensure backstack is cleared up to home
                .build()
            navController.navigate(R.id.navigation_scan, null, options)

            // Clear selection state of all menu items
            for (i in 0 until navView.menu.size()) {
                navView.menu.getItem(i).isChecked = false
            }

            // Clear last selected item ID as no menu item is selected
            lastSelectedItemId = -1
        }

// Handle bottom navigation click events
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    Log.d("Navigation", "Navigating to HomeFragment")
                    navController.navigate(R.id.navigation_home)
                    lastSelectedItemId = R.id.navigation_home
                    true
                }
                R.id.navigation_notification -> {
                    Log.d("Navigation", "Navigating to NotificationFragment")
                    navController.navigate(R.id.navigation_notification)
                    lastSelectedItemId = R.id.navigation_notification
                    true
                }
                R.id.navigation_profile -> {
                    Log.d("Navigation", "Navigating to ProfileFragment")
                    navController.navigate(R.id.navigation_profile)
                    lastSelectedItemId = R.id.navigation_profile
                    true
                }
                R.id.navigation_history -> {
                    Log.d("Navigation", "Navigating to HistoryFragment")
                    navController.navigate(R.id.navigation_history)
                    lastSelectedItemId = R.id.navigation_history
                    true
                }
                else -> false
            }
        }

    }
}
