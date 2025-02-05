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
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.internal.LinkedTreeMap

class HomeAdapter(
    private val onAddToPlaylistClicked: (VideoYT.VideoYTItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<VideoYT.VideoYTItem>()
    class HomeViewHolder(itemView: ItemHomeBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val binding = itemView

        fun bind(item: VideoYT.VideoYTItem, onAddToPlaylistClicked: (VideoYT.VideoYTItem) -> Unit) {
            Glide.with(binding.root)
                .load(item.snippet.thumbnails.high.url)
                .into(binding.imageViewItemHome)
            binding.textViewTitleItemHome.text = item.snippet.title

//            binding.textViewDateItemHome.text = item.snippet.publishedAt

            // evento de clique no item para abrir a tela de vídeo
            binding.imageViewItemHome.setOnClickListener {
                val intent = Intent(it.context, PlayerActivity::class.java)

                // Converte o videoId para String
                val videoIdToSend = convertVideoId(item)

                intent.putExtra("video_id", videoIdToSend)
                intent.putExtra("title", item.snippet.title)
                intent.putExtra("thumbnail", item.snippet.thumbnails.high.url)
                it.context.startActivity(intent)
            }

            binding.imageViewAddToPlaylist.setOnClickListener {
                item.videoId = convertVideoId(item)!!
                onAddToPlaylistClicked(item) // Passa o vídeo para o listener
            }
        }

        private fun convertVideoId(item: VideoYT.VideoYTItem) : String? {
            return when (val id = item.videoId) {
                is String -> id // Caso seja uma String, passa diretamente
                is LinkedTreeMap<*, *> -> {
                    // Se for um LinkedTreeMap, converta para JsonObject
                    val jsonObject = JsonObject().apply {
                        id.forEach { (key, value) ->
                            this.add(key.toString(), convertToJsonElement(value)) // Adiciona corretamente os valores
                        }
                    }

                    // Agora pode acessar os campos do JsonObject como normalmente faria
                    val channelId = jsonObject.getAsJsonPrimitive("videoId").asString
                    channelId // Passa o channelId ou outro campo que você desejar
                }
                else -> null // Caso seja um tipo inesperado
            }
        }

        // Função auxiliar para converter valores para JsonElement
        private fun convertToJsonElement(value: Any): JsonElement {
            return when (value) {
                is String -> JsonPrimitive(value) // Converte String para JsonPrimitive
                is Number -> JsonPrimitive(value) // Converte Number para JsonPrimitive
                is Boolean -> JsonPrimitive(value) // Converte Boolean para JsonPrimitive
                is LinkedTreeMap<*, *> -> {
                    val jsonObject = JsonObject()
                    value.forEach { (key, subValue) ->
                        jsonObject.add(key.toString(), convertToJsonElement(subValue)) // Recursão para sub-objetos
                    }
                    jsonObject // Retorna o JsonObject
                }
                else -> JsonPrimitive(value.toString()) // Para qualquer outro tipo, converte para String
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
        (holder as HomeViewHolder).bind(items[position], onAddToPlaylistClicked)
    }

    fun setData(data: List<VideoYT.VideoYTItem>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }
}