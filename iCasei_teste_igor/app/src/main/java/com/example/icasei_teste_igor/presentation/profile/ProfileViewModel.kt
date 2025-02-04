package com.example.icasei_teste_igor.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icasei_teste_igor.data.local.FavoriteVideoDao
import com.example.icasei_teste_igor.domain.model.FavoriteVideo
import com.example.icasei_teste_igor.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val favoriteDao: FavoriteVideoDao) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user.asStateFlow()

    private val _favoriteVideos = MutableStateFlow<List<FavoriteVideo>>(emptyList())
    val favoriteVideos: StateFlow<List<FavoriteVideo>> get() = _favoriteVideos

    private val _isFavoriteState = MutableStateFlow(false)
    val isFavoriteState: StateFlow<Boolean> get() = _isFavoriteState

    init {
        // Verificar se o usuário está logado
        checkLoggedUser()
    }

    private fun checkLoggedUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _user.value =
                User(currentUser.uid,
                    currentUser.displayName ?: "Usuário",
                    currentUser.email ?: "",
                    currentUser.photoUrl?.toString())
            loadFavorites()
        }
    }

    fun login() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _user.value =
                User(currentUser.uid,
                    currentUser.displayName ?: "Usuário",
                    currentUser.email ?: "",
                    currentUser.photoUrl?.toString())
            loadFavorites()
        } else {
            _user.value = null
        }
    }

    fun logout() {
        auth.signOut()
        _user.value = null
        _favoriteVideos.value = emptyList()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _user.value?.let { user ->
                favoriteDao.getAllFavorites(user.uid).collect { videos ->
                    _favoriteVideos.value = videos
                }
            }
        }
    }

    fun isFavorite(videoId: String, userId: String): Flow<Boolean> {
        return favoriteDao.isFavorite(videoId, userId)
    }

    // Função para alternar o favorito
    fun toggleFavorite(video: FavoriteVideo) {
        viewModelScope.launch {
            _user.value?.let { user ->
                val existingFavorite = favoriteDao.getFavoriteByIdSync(video.videoId, user.uid)
                if (existingFavorite == null) {
                    favoriteDao.insertFavorite(video.copy(userId = user.uid))
                } else {
                    favoriteDao.removeFavorite(existingFavorite)
                }
                loadFavorites() // Atualiza a lista após a modificação
            }
        }
    }
}