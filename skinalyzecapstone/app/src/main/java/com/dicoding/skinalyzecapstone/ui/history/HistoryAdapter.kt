package com.dicoding.skinalyzecapstone.ui.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.skinalyzecapstone.R
import com.dicoding.skinalyzecapstone.databinding.ItemHistoryBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val historyList = mutableListOf<HistoryItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(items: List<HistoryItem>) {
        historyList.clear()
        historyList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int = historyList.size

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HistoryItem) {
            binding.historyTitle.text = item.title
            binding.historyDate.text = item.date
        }
    }

    data class HistoryItem(val title: String, val date: String)
}
