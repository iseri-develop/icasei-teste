package com.example.icasei_teste_igor.presentation.home.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.icasei_teste_igor.databinding.ItemHomeBinding
import com.example.icasei_teste_igor.domain.model.VideoYT
import com.example.icasei_teste_igor.presentation.PlayerActivity
import com.google.gson.JsonElement

class HomeAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<VideoYT.VideoYTItem>()

    class HomeViewHolder(itemView: ItemHomeBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val binding = itemView

        fun bind(item: VideoYT.VideoYTItem) {
            Glide.with(binding.root)
                .load(item.snippet.thumbnails.high.url)
                .into(binding.imageViewItemHome)
            binding.textViewTitleItemHome.text = item.snippet.title

            binding.textViewDateItemHome.text = item.snippet.publishedAt

            // evento de clique no item para abrir a tela de vídeo
            binding.imageViewItemHome.setOnClickListener {
                val intent = Intent(it.context, PlayerActivity::class.java)

                // Verifica se o id é do tipo StringId ou ChannelId
                val videoIdToSend = when (val id = item.videoId) {
                    is String -> id
                    is JsonElement -> id.asJsonObject.getAsJsonPrimitive("channelId").asString
                    else -> null
                }

                intent.putExtra("video_id", videoIdToSend)
                intent.putExtra("title", item.snippet.title)
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HomeViewHolder).bind(items[position])
    }

    fun setData(data: List<VideoYT.VideoYTItem>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }
}