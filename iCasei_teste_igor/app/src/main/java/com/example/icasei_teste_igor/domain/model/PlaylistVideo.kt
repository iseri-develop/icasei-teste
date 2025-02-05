package com.example.icasei_teste_igor.domain.model

import androidx.room.Entity

@Entity(
    tableName = "playlist_video",
    primaryKeys = ["playlistId", "videoId"]
)
data class PlaylistVideo(
    val playlistId: Long,
    val videoId: String
)