package com.dicoding.asclepius.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.local.room.HistoryDao

class HistoryRepository private constructor(
    private val historyDao: HistoryDao
) {
    fun getAllHistory() : LiveData<Result<List<HistoryEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val history = historyDao.getAllHistory()
            Log.d("HistoryRepository", "getAllHistory: $history")
            emit(Result.Success(history))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun insertHistory(newHistory: HistoryEntity) : LiveData<Result<HistoryEntity>> = liveData {
        Log.d("HistoryRepository", "insertHistory: $newHistory")
        emit(Result.Loading)
        try {
            historyDao.insertHistory(newHistory)
            emit(Result.SuccessMessage("History saved successfully"))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: HistoryRepository? = null
        fun getInstance(
            historyDao: HistoryDao,
        ): HistoryRepository =
            instance ?: synchronized(this) {
                instance ?: HistoryRepository(historyDao)
            }.also { instance = it }
    }
}