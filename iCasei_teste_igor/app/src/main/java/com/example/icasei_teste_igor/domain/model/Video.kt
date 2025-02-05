package com.example.icasei_teste_igor.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class Video(
    @PrimaryKey
    val videoId: String,
    val title: String,
    val thumbnailUrl: String
)

