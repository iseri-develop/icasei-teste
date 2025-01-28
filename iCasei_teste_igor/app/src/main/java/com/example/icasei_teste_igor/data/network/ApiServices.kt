package com.example.icasei_teste_igor.data.network

import com.example.icasei_teste_igor.domain.model.ChannelModel
import com.example.icasei_teste_igor.domain.model.VideoYT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("videos")
    fun getListVideos(
        @Query("part") part: String,
        @Query("chart") chart: String,
    ) : Call<VideoYT>

    @GET("channels")
    fun getChannel(
        @Query("part") part: String,
        @Query("id") id: String
    ) : Call<ChannelModel>

    @GET("search")
    fun getVideo(
        @Query("part") part: String,
        @Query("channelId") channelId: String,
        @Query("order") order: String
    ) : Call<VideoYT>
}