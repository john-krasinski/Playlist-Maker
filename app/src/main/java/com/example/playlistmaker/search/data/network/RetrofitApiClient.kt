package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.ApiClient
import com.example.playlistmaker.search.data.dto.ApiResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RetrofitApiClient(private val searchApi: SearchApi): ApiClient {

    override suspend fun doRequest(dto: Any): ApiResponse {

        if (dto is TrackSearchRequest) {

            return withContext(Dispatchers.IO) {
                try {
                    searchApi.doSearch(dto.query).apply { resultCode = 200 }
                } catch (e: Throwable) {
                    ApiResponse().apply { resultCode = 500 }
                }
            }

        } else {
            return ApiResponse().apply { resultCode = 400 }
        }
    }
}