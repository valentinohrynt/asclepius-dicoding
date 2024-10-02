package com.dicoding.asclepius.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
class HistoryEntity (
    @PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "id")
    val id: Int = 0,

    @field:ColumnInfo(name = "image_uri", typeAffinity = ColumnInfo.BLOB)
    val image: ByteArray,

    @field:ColumnInfo(name = "category")
    val category: String,

    @field:ColumnInfo(name = "confidence_score")
    val confidenceScore: Float,

    @field:ColumnInfo(name = "date_time")
    val dateTime: String
)