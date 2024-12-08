package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.ApiResponse
import com.example.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("search?entity=song")
    public fun doSearch(
        @Query("term") text: String
    ) : Call<TrackSearchResponse>
}