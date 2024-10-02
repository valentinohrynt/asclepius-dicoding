package com.dicoding.asclepius.view.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.NewsEntity
import com.dicoding.asclepius.databinding.NewsItemBinding
import com.dicoding.asclepius.view.MainViewModel

class NewsAdapter(
    private val viewModel: MainViewModel
): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val newsList = mutableListOf<NewsEntity>()

    fun setNewsList(newsList: List<NewsEntity>){
        val diffCallback = NewsDiffCallback(this.newsList, newsList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.newsList.clear()
        this.newsList.addAll(newsList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsAdapter.NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.bind(news)
    }

    override fun getItemCount(): Int = newsList.size

    inner class NewsViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        private val binding = NewsItemBinding.bind(view)

        private val ivImage = binding.imageView
        private val tvTitle = binding.title
        private val tvDescription = binding.description

        fun bind(news: NewsEntity) {

            tvTitle.text = news.title
            tvDescription.text = news.description

            Glide.with(view.context)
                .load(news.urlToImage)
                .into(ivImage)

            view.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
                view.context.startActivity(intent)
            }
        }

    }

    private class NewsDiffCallback(
        private val oldList: List<NewsEntity>,
        private val newList: List<NewsEntity>
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