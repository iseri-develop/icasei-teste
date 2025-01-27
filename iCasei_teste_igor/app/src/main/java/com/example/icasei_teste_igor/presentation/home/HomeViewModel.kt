package com.example.icasei_teste_igor.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.icasei_teste_igor.data.network.ApiConfig
import com.example.icasei_teste_igor.domain.model.ChannelModel
import retrofit2.Call
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _channel = MutableLiveData<ChannelModel?>()
    val channel: MutableLiveData<ChannelModel?> = _channel

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    init {
        getChannel()
    }

    fun getChannel() {
        val client = ApiConfig.getService().getChannel(
            part = "snippet",
            id = "UC_x5XG1OV2P6uZZ5FSM9Ttw"
        )

        client.enqueue(object : retrofit2.Callback<ChannelModel> {
            override fun onResponse(call: Call<ChannelModel>, response: Response<ChannelModel>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _channel.value = data
                    } else {
                        _message.value = "No channel"
                    }
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<ChannelModel>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())

                _isLoading.value = false
                _message.value = t.message.toString()
            }
        })
    }
}