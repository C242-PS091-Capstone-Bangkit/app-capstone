package com.dicoding.skinalyzecapstone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dicoding.skinalyzecapstone.data.pref.UserPreference
import com.dicoding.skinalyzecapstone.databinding.ActivityMainBinding
import com.dicoding.skinalyzecapstone.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

private val AppCompatActivity.dataStore by preferencesDataStore(name = "user_preferences")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi DataStore Preference
        userPreference = UserPreference.getInstance(dataStore)

        // Cek status login sebelum menampilkan konten utama
        checkLoginStatus()

        // Inflate layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Bottom Navigation dan NavController
        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val navView: BottomNavigationView = binding.bottomNavigation
        navView.setupWithNavController(navController)

        var lastSelectedItemId = R.id.navigation_home

        // Default NavOptions with animations
        val navOptionsWithAnimation = NavOptions.Builder()
            .setEnterAnim(R.anim.fade_in)
            .setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.fade_out)
            .build()

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home, null, navOptionsWithAnimation)
                    lastSelectedItemId = R.id.navigation_home
                    true
                }
                R.id.navigation_notification -> {
                    navController.navigate(R.id.navigation_notification, null, navOptionsWithAnimation)
                    lastSelectedItemId = R.id.navigation_notification
                    true
                }
                R.id.navigation_profile -> {
                    navController.navigate(R.id.navigation_profile, null, navOptionsWithAnimation)
                    lastSelectedItemId = R.id.navigation_profile
                    true
                }
                R.id.navigation_history -> {
                    navController.navigate(R.id.navigation_history, null, navOptionsWithAnimation)
                    lastSelectedItemId = R.id.navigation_history
                    true
                }
                else -> false
            }
        }

        binding.fab.setOnClickListener {
            Log.d("FAB_Click", "FAB clicked, navigating to ScanFragment")
            val options = NavOptions.Builder()
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .setPopUpTo(R.id.navigation_home, true)
                .build()
            navController.navigate(R.id.navigation_scan, null, options)
            clearMenuSelection(navView)
            lastSelectedItemId = -1
        }
    }


    private fun clearMenuSelection(navView: BottomNavigationView) {
        for (i in 0 until navView.menu.size()) {
            navView.menu.getItem(i).isChecked = false
        }
    }

    private fun checkLoginStatus() {
        lifecycleScope.launch {
            userPreference.getSession().collect { user ->
                if (!user.isLogin) {
                    // Jika pengguna belum login, arahkan ke LoginActivity
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                } else {
                    // Menampilkan log informasi pengguna yang login
                    Log.d("UserDetails", "User logged in: ")
                    Log.d("UserDetails", "ID: ${user.idUser}")
                    Log.d("UserDetails", "Username: ${user.name}")
                    Log.d("UserDetails", "Email: ${user.email}")
                    Log.d("UserDetails", "Token: ${user.token}")
                    Log.d("UserDetails", "Is Logged In: ${user.isLogin}")
                }
            }
        }
    }

}
