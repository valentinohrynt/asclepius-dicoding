package com.dicoding.asclepius.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.local.entity.NewsEntity

@Database(entities = [HistoryEntity::class, NewsEntity::class], version = 1, exportSchema = false)
abstract class AsclepiusDB : RoomDatabase() {

    abstract fun historyDao(): HistoryDao
    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var instance: AsclepiusDB? = null

        fun getInstance(context: Context): AsclepiusDB {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AsclepiusDB::class.java, "Asclepius.db"
                ).fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
        }
    }
}
