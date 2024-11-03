@file:Suppress("DEPRECATION")

package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.utils.Utils.showToast
import com.dicoding.asclepius.view.adapter.NewsAdapter

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var resultImage: ImageView
    private lateinit var resultText: TextView
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageUri = intent.getParcelableExtra<Uri>(EXTRA_IMAGE_URI)
        val prediction = intent.getStringExtra(EXTRA_PREDICTION)

        resultImage = binding.resultImage
        resultText = binding.resultText

        resultImage.setImageURI(imageUri)
        resultText.text = prediction

        progressBar = binding.progressBar
        newsRecyclerView = binding.newsRecyclerView
        newsAdapter = NewsAdapter(viewModel)

        newsRecyclerView.layoutManager = LinearLayoutManager(this)
        newsRecyclerView.setHasFixedSize(true)
        newsRecyclerView.adapter = newsAdapter

        viewModel.getNews().observe(this) { newsList ->
            if (newsList != null){
                when (newsList) {
                    is Result.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        progressBar.visibility = View.GONE
                        newsAdapter.setNewsList(newsList.data)
                    }
                    is Result.Error -> {
                        progressBar.visibility = View.GONE
                        showToast(this,newsList.error)
                    }
                    else -> {
                        progressBar.visibility = View.GONE
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

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image"
        const val EXTRA_PREDICTION = "extra_prediction"
    }
}