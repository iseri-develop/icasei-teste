package com.example.icasei_teste_igor.presentation.playlistdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icasei_teste_igor.data.local.PlaylistDao
import com.example.icasei_teste_igor.domain.model.Video
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistDetailViewModel(private val playlistDao: PlaylistDao) : ViewModel() {

    private val _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos: StateFlow<List<Video>> get() = _videos.asStateFlow()

    fun loadVideos(playlistId: Long) {
        viewModelScope.launch {
            _videos.value = playlistDao.getVideosForPlaylist(playlistId)
        }
    }

    fun removeVideoFromPlaylist(playlistId: Long, videoId: String) {
        viewModelScope.launch {
            playlistDao.removeVideoFromPlaylist(playlistId, videoId)
            loadVideos(playlistId)
        }
    }

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            playlistDao.deletePlaylistById(playlistId)
        }
    }
}
