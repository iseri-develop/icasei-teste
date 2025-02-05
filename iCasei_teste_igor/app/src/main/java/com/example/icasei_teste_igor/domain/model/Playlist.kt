package com.example.icasei_teste_igor.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val userId: String // Identifica o dono da playlist
)
