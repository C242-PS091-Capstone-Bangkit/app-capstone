package com.dicoding.skinalyzecapstone.ui.result

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.skinalyzecapstone.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the image URI from the intent
        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)?.let { Uri.parse(it) }

        // Display the image
        imageUri?.let {
            Log.d("ResultActivity", "Displaying image URI: $it")
            binding.resultImage.setImageURI(it) // Ensure resultImage is defined in your layout
        } ?: run {
            Log.d("ResultActivity", "No image URI found")
        }

        // Optional: Display confidence score or analysis result if passed
        val resultText = intent.getStringExtra(EXTRA_RESULT_TEXT) ?: "No result available"
        binding.resultText.text = resultText

        val confidenceText = intent.getStringExtra(EXTRA_CONFIDENCE_TEXT) ?: "Confidence: N/A"
        binding.confidenceText.text = confidenceText
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT_TEXT = "extra_result_text"
        const val EXTRA_CONFIDENCE_TEXT = "extra_confidence_text"
    }
}
