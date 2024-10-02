package com.dicoding.asclepius.view

import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.HistoryRepository
import com.dicoding.asclepius.data.NewsRepository
import com.dicoding.asclepius.data.local.entity.HistoryEntity

class MainViewModel(private val historyRepository: HistoryRepository, private val newsRepository: NewsRepository): ViewModel() {

    fun getAllHistory() = historyRepository.getAllHistory()

    fun insertHistory(history: HistoryEntity) = historyRepository.insertHistory(history)

    fun getNews() = newsRepository.getAllNews()
}