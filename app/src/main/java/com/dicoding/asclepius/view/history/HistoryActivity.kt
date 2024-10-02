package com.dicoding.asclepius.view.history

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.utils.Utils.showToast
import com.dicoding.asclepius.view.MainViewModel
import com.dicoding.asclepius.view.ViewModelFactory
import com.dicoding.asclepius.view.adapter.HistoryAdapter

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var progressBar: ProgressBar
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        historyRecyclerView = binding.historyRecyclerView
        historyAdapter = HistoryAdapter(viewModel)
        historyRecyclerView.adapter = historyAdapter
        historyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        historyRecyclerView.setHasFixedSize(true)

        progressBar = binding.progressBar

        viewModel.getAllHistory().observe(this) { historyList ->
            if (historyList != null) {
                when (historyList) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        historyAdapter.setHistoryList(historyList.data)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(this, historyList.error)
                    } else -> {
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