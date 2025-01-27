package com.example.icasei_teste_igor.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ChannelModel(
    @SerializedName("items")
    val items: List<Item>
) {
    data class Item(
        @SerializedName("snippet")
        val snippet: Snippet
    )
}