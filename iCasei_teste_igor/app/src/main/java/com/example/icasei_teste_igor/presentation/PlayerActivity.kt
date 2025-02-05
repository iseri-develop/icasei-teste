package com.example.icasei_teste_igor.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.icasei_teste_igor.R
import com.example.icasei_teste_igor.data.local.AppDatabase
import com.example.icasei_teste_igor.databinding.ActivityPlayerBinding
import com.example.icasei_teste_igor.domain.model.FavoriteVideo
import com.example.icasei_teste_igor.presentation.profile.FavoriteVideoViewModelFactory
import com.example.icasei_teste_igor.presentation.profile.ProfileViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.launch

class PlayerActivity : AppCompatActivity() {

    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels {
        FavoriteVideoViewModelFactory(AppDatabase.getDatabase(this).favoriteVideoDao())
    }

    private var videoId: String? = null
    private var videoTitle: String? = null
    private var videoThumbnail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEnabled) {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoId = intent.getStringExtra("video_id")
        videoTitle = intent.getStringExtra("title")
        videoThumbnail = intent.getStringExtra("thumbnail")

        lifecycle.addObserver(binding.youtubePlayerView)

        binding.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                videoId?.let {
                    youTubePlayer.loadVideo(it, 0f)
                }
            }
        })

        binding.textViewTitlePlayer.text = intent.getStringExtra("title")

        setupFavoriteVideo()
        setupFavoriteButton()
    }

    private fun setupFavoriteVideo() {
        videoId?.let { id ->
            lifecycleScope.launch {
                profileViewModel.user.collect { user ->
                    if (user != null) {
                        profileViewModel.isFavorite(id, user.uid).collect { isFav ->
                            val icon = if (isFav) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
                            binding.buttonFavorite.setImageResource(icon)

                            binding.buttonFavorite.setOnClickListener {
                                toggleFavorite()
                            }
                        }
                    } else {
                        binding.buttonFavorite.setOnClickListener {
                            // Usuário não autenticado - exibir mensagem
                            Toast.makeText(this@PlayerActivity, "É necessário estar logado para favoritar vídeos.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupFavoriteButton() {
        // Configurando o clique no botão para alternar o favorito
        binding.buttonFavorite.setOnClickListener {
            toggleFavorite()  // Chama a função de toggle
        }
    }

    private fun toggleFavorite() {
        val user = profileViewModel.user.value
        if (user == null) {
            Toast.makeText(this, "É necessário estar logado para favoritar vídeos.", Toast.LENGTH_SHORT).show()
            return
        }

        videoId?.let { id ->
            profileViewModel.toggleFavorite(
                FavoriteVideo(
                    videoId = id,
                    title = videoTitle ?: "",
                    thumbnailUrl = videoThumbnail ?: "",
                    userId = user.uid
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}