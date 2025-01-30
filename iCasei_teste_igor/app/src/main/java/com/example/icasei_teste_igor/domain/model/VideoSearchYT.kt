package com.example.icasei_teste_igor.domain.model

import com.google.gson.annotations.SerializedName

class VideoSearchYT (
    @SerializedName("items")
    val items: List<VideoYTItem>
){
    data class VideoYTItem(

        @SerializedName("id")
        val id: VideoIdItem,

        @SerializedName("snippet")
        val snippet: Snippet
    )

    data class VideoIdItem(
        @SerializedName("videoId")
        val videoId: String
    )
}