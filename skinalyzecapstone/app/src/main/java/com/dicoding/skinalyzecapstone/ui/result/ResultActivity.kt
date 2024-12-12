package com.dicoding.skinalyzecapstone.ui.result

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.skinalyzecapstone.R
import com.dicoding.skinalyzecapstone.data.response.PredictResponse
import com.dicoding.skinalyzecapstone.ui.adapter.RecommendationAdapter

class ResultActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RESULT = "extra_result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val result = intent.getParcelableExtra<PredictResponse>(EXTRA_RESULT)

        result?.let {
            // Display skin result
            findViewById<TextView>(R.id.result_text).text = it.skinType
            findViewById<TextView>(R.id.confidenceText).text = it.skinCondition

            // Display recommendations using RecyclerView
            val recommendationList = findViewById<RecyclerView>(R.id.recommendation_list)
            recommendationList.layoutManager = LinearLayoutManager(this)
            val adapter = RecommendationAdapter(it.recommendation ?: emptyList()) // Use emptyList if null
            recommendationList.adapter = adapter
        }
    }
}