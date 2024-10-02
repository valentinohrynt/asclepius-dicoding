package com.dicoding.asclepius.view.news

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.databinding.ActivityNewsBinding
import com.dicoding.asclepius.utils.Utils.showToast
import com.dicoding.asclepius.view.MainViewModel
import com.dicoding.asclepius.view.ViewModelFactory
import com.dicoding.asclepius.view.adapter.NewsAdapter

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        newsAdapter = NewsAdapter(viewModel)
        newsRecyclerView = binding.newsRecyclerView
        newsRecyclerView.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        newsRecyclerView.adapter = newsAdapter

        progressBar = binding.progressBar

        viewModel.getNews().observe(this) { newsList ->
            if (newsList != null) {
                when (newsList) {
                    Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        newsAdapter.setNewsList(newsList.data)
                        }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(this,newsList.error)
                    }
                    else -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(this,"Something went wrong")
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}