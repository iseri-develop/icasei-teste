package com.example.icasei_teste_igor.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Snippet(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("customUrl")
    val customUrl: String,
    @SerializedName("publisedAt")
    val publishedAt: String,
    @SerializedName("thumbnails")
    val thumbnails: ThumbnailsYT,
    @SerializedName("country")
    val country: String
)
