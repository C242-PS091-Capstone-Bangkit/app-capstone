package com.dicoding.skinalyzecapstone.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.skinalyzecapstone.R
import com.dicoding.skinalyzecapstone.data.response.RecommendationItem
import com.dicoding.skinalyzecapstone.ui.detail.DetailActivity

class RecommendationAdapter(private val recommendations: List<RecommendationItem>, private val skinCondition: String?) :


    RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        val productName: TextView = itemView.findViewById(R.id.product_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommendation, parent, false)
        return RecommendationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val recommendation = recommendations[position]
        Glide.with(holder.itemView.context).load(recommendation.gambarProduk).into(holder.productImage)
        holder.productName.text = recommendation.namaProduk

        // In RecommendationAdapter.kt

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_RECOMMENDATION, recommendation)
            intent.putExtra(DetailActivity.EXTRA_SKIN_CONDITION, skinCondition) // Pass only the string
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = recommendations.size
}