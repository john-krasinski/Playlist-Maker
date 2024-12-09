package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.ApiResponse

interface ApiClient {

    fun doRequest(dto: Any, onSuccess: (ApiResponse) -> Unit, onError: (String) -> Unit)
}