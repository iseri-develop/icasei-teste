package com.example.icasei_teste_igor.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class VideoYT(
    @SerializedName("items")
    val items: List<VideoYTItem>
){
    data class VideoYTItem(
        @SerializedName("id")
        var videoId: Any,

        @SerializedName("snippet")
        val snippet: Snippet
    )
}