package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.ApiResponse

interface ApiClient {

    fun doRequest(dto: Any, onSuccess: (ApiResponse) -> Unit, onError: (String) -> Unit)
//    fun doRequest(dto: Any): ApiResponse
}