package com.dicoding.skinalyzecapstone.ui.help

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.skinalyzecapstone.databinding.ActivityHelpBinding

class HelpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}