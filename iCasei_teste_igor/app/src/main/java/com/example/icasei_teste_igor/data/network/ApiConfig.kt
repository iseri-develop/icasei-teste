package com.example.icasei_teste_igor.data.network

import com.example.icasei_teste_igor.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor

class ApiConfig {

    companion object {
        fun getService(): ApiServices {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
                )

            val client = okhttp3.OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor { chain ->
                    val url = chain
                        .request()
                        .url
                        .newBuilder()
                        .addQueryParameter("key", BuildConfig.YOUTUBE_API_KEY)
                        .build()

                    chain.proceed(chain.request().newBuilder().url(url).build())
                }
                .build()

            val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiServices::class.java)
        }
    }
}