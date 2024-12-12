package com.dicoding.skinalyzecapstone.ui.result

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.skinalyzecapstone.data.response.PredictResponse
import com.dicoding.skinalyzecapstone.databinding.ActivityResultBinding
import com.dicoding.skinalyzecapstone.ui.adapter.RecommendationAdapter

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    companion object {
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = intent.getParcelableExtra<PredictResponse>(EXTRA_RESULT)
        val imageUri = intent.getParcelableExtra<Uri>(EXTRA_IMAGE_URI)

        result?.let {
            binding.resultImage.setImageURI(imageUri)
            binding.resultText.text = it.skinType

            val saranKandungan = it.recommendation?.firstOrNull()?.saranKandungan
            val fullConditionText = if (saranKandungan != null) {
                "Anda memiliki kulit berkondisi ${it.skinCondition}\nSaran Kandungan: $saranKandungan"
            } else {
                "Anda memiliki kulit berkondisi ${it.skinCondition}"
            }
            binding.confidenceText.text = fullConditionText

            binding.recommendationList.layoutManager = LinearLayoutManager(this)
            val updatedRecommendations = it.recommendation?.toMutableList()?.also { list ->
                if (list.isNotEmpty()) {
                    list.removeAt(0)
                }
            } ?: emptyList()

            val adapter = RecommendationAdapter(updatedRecommendations, it.skinCondition) // Pass the string here as well
            binding.recommendationList.adapter = adapter
        }
    }
}