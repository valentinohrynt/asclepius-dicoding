package com.dicoding.asclepius.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.asclepius.data.local.entity.NewsEntity
import com.dicoding.asclepius.data.local.room.NewsDao
import com.dicoding.asclepius.data.remote.retrofit.ApiService

class NewsRepository private constructor(
    private val apiService: ApiService,
    private val newsDao: NewsDao
) {
    fun getAllNews(): LiveData<Result<List<NewsEntity>>> = liveData {
        emit(Result.Loading)

        try {
            val localData = newsDao.getAllNews()

            if (localData.isNotEmpty()) {
                emit(Result.Success(localData))
            } else {
                try {
                    val response = apiService.getNews("cancer", "health")
                    val news = response.articles
                    val newsList = news.map {
                        articlesItem ->
                        val id = news.indexOf(articlesItem) + 1
                        NewsEntity(
                            id,
                            articlesItem.publishedAt,
                            articlesItem.author,
                            articlesItem.urlToImage,
                            articlesItem.description,
                            articlesItem.source?.name,
                            articlesItem.title,
                            articlesItem.url,
                            articlesItem.content
                        )
                    }.filter { it.title != "[Removed]" }
                    newsDao.deleteAllNews()
                    newsDao.insertNews(newsList)
                    emit(Result.Success(newsList))
                } catch (e: Exception) {
                    emit(Result.Error(e.message.toString()))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null
        fun getInstance(
            apiService: ApiService,
            newsDao: NewsDao
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService, newsDao)
            }.also { instance = it }
    }
}