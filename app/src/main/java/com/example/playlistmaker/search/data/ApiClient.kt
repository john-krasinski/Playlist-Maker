package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.ApiResponse

interface ApiClient {

    suspend fun doRequest(dto: Any): ApiResponse
}