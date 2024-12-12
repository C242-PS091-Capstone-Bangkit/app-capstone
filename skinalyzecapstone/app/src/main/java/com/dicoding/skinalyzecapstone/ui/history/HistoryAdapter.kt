package com.dicoding.skinalyzecapstone.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.skinalyzecapstone.data.response.GetHistoryResponse
import com.dicoding.skinalyzecapstone.databinding.ItemHistoryBinding

class HistoryAdapter(private val historyList: List<GetHistoryResponse>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = historyList[position]
        with(holder.binding) {
            tvProductName.text = history.namaProduk
            tvSkinType.text = history.skinType
            tvSkinCondition.text = history.skinCondition

            Glide.with(holder.itemView.context)
                .load(history.gambarProduk)
                .into(ivProductImage)

            // Tambahkan elemen lain yang ingin ditampilkan di item history
        }
    }

    override fun getItemCount(): Int = historyList.size
}