package com.dicoding.asclepius.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.HistoryItemBinding
import com.dicoding.asclepius.utils.Utils.convertToPercent
import com.dicoding.asclepius.view.MainViewModel

class HistoryAdapter(
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val historyList = mutableListOf<HistoryEntity>()

    fun setHistoryList(historyList: List<HistoryEntity>){
        val diffCallback = HistoryDiffCallback(this.historyList, historyList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.historyList.clear()
        this.historyList.addAll(historyList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = historyList[position]
        holder.bind(history)
    }

    override fun getItemCount(): Int = historyList.size

    inner class HistoryViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        private val binding = HistoryItemBinding.bind(view)

        private val ivImage = binding.ivImage
        private val tvDateTime = binding.tvDateTime
        private val tvCategory = binding.tvCategory
        private val tvConfidenceScore = binding.tvConfidenceScore

        @SuppressLint("SetTextI18n")
        fun bind(history: HistoryEntity) {

            tvDateTime.text = history.dateTime
            tvCategory.text = "Category: ${history.category}"
            tvConfidenceScore.text = "Confidence Score: ${convertToPercent(history.confidenceScore)}"

            Glide.with(view.context)
                .load(history.image)
                .into(ivImage)
        }

    }

    private class HistoryDiffCallback(
        private val oldList: List<HistoryEntity>,
        private val newList: List<HistoryEntity>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}