package com.dicoding.asclepius.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
class NewsEntity(
    @PrimaryKey
    @field:ColumnInfo(name = "id")
    val id: Int = 0,

    @field:ColumnInfo(name = "published_at")
    val publishedAt: String? = null,

    @field:ColumnInfo(name = "author")
    val author: String? = null,

    @field:ColumnInfo(name = "url_to_image")
    val urlToImage: String? = null,

    @field:ColumnInfo(name = "description")
    val description: String? = null,

    @field:ColumnInfo(name = "source")
    val source: String? = null,

    @field:ColumnInfo(name = "title")
    val title: String? = null,

    @field:ColumnInfo(name = "url")
    val url: String? = null,

    @field:ColumnInfo(name = "content")
    val content: String? = null
)