package com.example.icasei_teste_igor.presentation.playlistdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.icasei_teste_igor.data.local.PlaylistDao

class PlaylistDetailViewModelFactory(private val playlistDao: PlaylistDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaylistDetailViewModel::class.java)) {
            return PlaylistDetailViewModel(playlistDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
