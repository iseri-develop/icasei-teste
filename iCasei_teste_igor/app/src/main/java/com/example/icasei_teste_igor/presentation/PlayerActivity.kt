package com.example.icasei_teste_igor.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.icasei_teste_igor.databinding.ActivityPlayerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener


class PlayerActivity : AppCompatActivity() {

    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar?.setDisplayHomeAsUpEnabled(true)

        lifecycle.addObserver(binding.youtubePlayerView)

        binding.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = intent.getStringExtra("video_id")
                if (!videoId.isNullOrEmpty()) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            }
        })

        binding.textViewTitlePlayer.text = intent.getStringExtra("title")
    }
}