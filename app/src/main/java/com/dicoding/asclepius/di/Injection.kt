package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.HistoryRepository
import com.dicoding.asclepius.data.NewsRepository
import com.dicoding.asclepius.data.local.room.AsclepiusDB
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig

object Injection {
    fun provideHistoryRepository(context: Context): HistoryRepository {
        val database = AsclepiusDB.getInstance(context)
        val dao = database.historyDao()
        return HistoryRepository.getInstance(dao)
    }
    fun provideNewsRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiService()
        val database = AsclepiusDB.getInstance(context)
        val dao = database.newsDao()
        return NewsRepository.getInstance(apiService, dao)
    }
}