package com.example.icasei_teste_igor.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icasei_teste_igor.data.network.ApiConfig
import com.example.icasei_teste_igor.domain.model.VideoYT
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _video = MutableLiveData<VideoYT?>()
    val video: MutableLiveData<VideoYT?> = _video

    private val _videoSearched = MutableLiveData<VideoYT?>()
    val videoSearched: MutableLiveData<VideoYT?> = _videoSearched

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    init {
        getVideoList()
    }

    private fun getVideoList() {
        viewModelScope.launch {
            _isLoading.value = true
            val client = ApiConfig.getService().getListVideos(
                part = "snippet",
                chart = "mostPopular"
            )

            client.enqueue(object : retrofit2.Callback<VideoYT> {
                override fun onResponse(call: Call<VideoYT>, response: Response<VideoYT>) {
                    _isLoading.value = false

                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            _video.value = data
                        } else {
                            _message.value = "No video"
                        }
                    } else {
                        _message.value = response.message()
                    }
                }

                override fun onFailure(call: Call<VideoYT>, t: Throwable) {
                    Log.e("HomeViewModel", "onFailure: ${t.message}")
                    _message.value = t.message
                }
            })
        }
    }

    fun getVideoSearched(query: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            val client = ApiConfig.getService().getVideoSearched(
                part = "snippet",
                query = query ?: "",
                maxResults = 10)

            client.enqueue(object : retrofit2.Callback<VideoYT> {
                override fun onResponse(call: Call<VideoYT>, response: Response<VideoYT>) {
                    _isLoading.value = false

                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            _videoSearched.value = data
                        } else {
                            _message.value = "No video"
                        }
                    }
                }

                override fun onFailure(call: Call<VideoYT>, t: Throwable) {
                    Log.e("HomeViewModel", "onFailure: ${t.message}")
                    _message.value = t.message
                }
            })
        }
    }
}