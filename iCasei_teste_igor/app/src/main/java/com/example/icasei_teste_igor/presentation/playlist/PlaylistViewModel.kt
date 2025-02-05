package com.example.icasei_teste_igor.presentation.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icasei_teste_igor.data.local.PlaylistDao
import com.example.icasei_teste_igor.domain.model.Playlist
import com.example.icasei_teste_igor.domain.model.Video
import com.example.icasei_teste_igor.domain.model.PlaylistVideo
import com.example.icasei_teste_igor.domain.model.User
import com.example.icasei_teste_igor.domain.model.VideoYT
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistDao: PlaylistDao) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> get() = _playlists.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user.asStateFlow()

    init {
        loadUser()
        loadUserPlaylists()
    }

    private fun getUserId(): String? {
        return auth.currentUser?.uid
    }

    fun loadUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _user.value =
                User(currentUser.uid,
                    currentUser.displayName ?: "Usuário",
                    currentUser.email ?: "",
                    currentUser.photoUrl?.toString())

            loadUserPlaylists()
        } else {
            _user.value = null
        }
    }

    private fun loadUserPlaylists() {
        viewModelScope.launch {
            getUserId()?.let { userId ->
                playlistDao.getAllPlaylists(userId).collect { playlists ->
                    _playlists.value = playlists
                }
            } ?: run {
                _playlists.value = emptyList()
            }
        }
    }

    fun createPlaylist(title: String) {
        viewModelScope.launch {
            getUserId()?.let { userId ->
                val newPlaylist = Playlist(title = title, userId = userId)
                playlistDao.insertPlaylist(newPlaylist)
                loadUserPlaylists()
            }
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            getUserId()?.let { userId ->
                if (playlist.userId == userId) {
                    playlistDao.deletePlaylistById(playlist.id)
                    loadUserPlaylists()
                }
            }
        }
    }

    fun addVideoToPlaylist(playlistId: Long, videoYTItem: VideoYT.VideoYTItem) {
        viewModelScope.launch {
            val video = Video(
                videoId = when (val id = videoYTItem.videoId) {
                    is String -> id
                    is Map<*, *> -> (id as? Map<*, *>)?.get("videoId") as? String ?: ""
                    else -> ""
                },
                title = videoYTItem.snippet.title,
                thumbnailUrl = videoYTItem.snippet.thumbnails.high.url
            )

            if (video.videoId.isNotEmpty()) {
                playlistDao.insertVideo(video) // Salvar o vídeo no banco local
                playlistDao.insertPlaylistVideo(PlaylistVideo(playlistId, video.videoId))
            }
        }
    }
}
