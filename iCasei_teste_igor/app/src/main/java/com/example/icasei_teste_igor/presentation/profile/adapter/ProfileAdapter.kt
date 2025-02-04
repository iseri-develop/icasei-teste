package com.example.icasei_teste_igor.presentation.profile.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.icasei_teste_igor.databinding.ItemProfileBinding
import com.example.icasei_teste_igor.domain.model.FavoriteVideo
import com.example.icasei_teste_igor.presentation.PlayerActivity

class ProfileAdapter(private val favoriteVideos: MutableList<FavoriteVideo>) :
    RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    inner class ProfileViewHolder(private val binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: FavoriteVideo) {
            binding.videoTitle.text = favorite.title
            // Carrega a imagem usando a biblioteca Glide
            Glide.with(binding.root)
                .load(favorite.thumbnailUrl)
                .into(binding.videoThumbnail)

            binding.root.setOnClickListener {
                val intent = Intent(it.context, PlayerActivity::class.java)
                intent.putExtra("video_id", favorite.videoId)
                intent.putExtra("title", favorite.title)
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(favoriteVideos[position])
    }

    override fun getItemCount(): Int = favoriteVideos.size

    fun updateFavorites(newFavorites: List<FavoriteVideo>) {
        favoriteVideos.clear()
        favoriteVideos.addAll(newFavorites)
        notifyDataSetChanged()
    }
}