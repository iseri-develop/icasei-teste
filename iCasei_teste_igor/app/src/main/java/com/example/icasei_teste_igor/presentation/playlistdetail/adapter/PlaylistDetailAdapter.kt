package com.example.icasei_teste_igor.presentation.playlistdetail.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.icasei_teste_igor.databinding.ItemVideoBinding
import com.example.icasei_teste_igor.domain.model.Video
import com.example.icasei_teste_igor.presentation.PlayerActivity

class PlaylistVideoAdapter(
    private val videos: List<Video>,
    private val onRemoveClick: (Video) -> Unit
) : RecyclerView.Adapter<PlaylistVideoAdapter.PlaylistVideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistVideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistVideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistVideoViewHolder, position: Int) {
        val video = videos[position]
        holder.bind(video, onRemoveClick)
    }

    override fun getItemCount() = videos.size

    class PlaylistVideoViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(video: Video, onRemoveClick: (Video) -> Unit) {
            binding.textViewVideoTitle.text = video.title
            Glide.with(binding.root)
                .load(video.thumbnailUrl)
                .into(binding.imageViewThumbnail)
            binding.buttonRemove.setOnClickListener {
                onRemoveClick(video)
            }

            binding.root.setOnClickListener {
                // Lógica para abrir a tela de vídeo
                val intent = Intent(it.context, PlayerActivity::class.java)

                intent.putExtra("video_id", video.videoId)
                intent.putExtra("title", video.title)
                intent.putExtra("thumbnail", video.thumbnailUrl)
                it.context.startActivity(intent)
            }
        }
    }
}
