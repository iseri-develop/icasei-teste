package com.example.icasei_teste_igor.presentation.playlistdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.icasei_teste_igor.data.local.PlaylistDao
import com.example.icasei_teste_igor.presentation.playlist.PlaylistViewModel

class PlaylistViewModelFactory(private val playlistDao: PlaylistDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlaylistViewModel(playlistDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
