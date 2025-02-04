package com.example.icasei_teste_igor.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_video")
data class FavoriteVideo(
    @PrimaryKey val videoId: String,
    val title: String,
    val thumbnailUrl: String,
    val userId: String
)