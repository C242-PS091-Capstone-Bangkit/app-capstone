package com.dicoding.skinalyzecapstone.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.skinalyzecapstone.R
import com.dicoding.skinalyzecapstone.data.response.RecommendationItem

class RecommendationAdapter(private val recommendations: List<RecommendationItem>) :
    RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productDetails: TextView = itemView.findViewById(R.id.product_details)
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
        holder.productDetails.text = "Contains: ${recommendation.saranKandungan}"
    }

    override fun getItemCount(): Int = recommendations.size
}