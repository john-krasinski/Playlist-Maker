package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.ApiClient
import com.example.playlistmaker.data.dto.ApiResponse
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitApiClient: ApiClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val searchApi = retrofit.create<SearchApi>()

    override fun doRequest(dto: Any, onSuccess: (ApiResponse) -> Unit, onError: (String) -> Unit) {

        if (dto is TrackSearchRequest) {

            searchApi.doSearch(dto.query).enqueue(object : Callback<TrackSearchResponse> {
                override fun onResponse(
                    call: Call<TrackSearchResponse>,
                    response: Response<TrackSearchResponse>
                ) {
                    val body = response.body() ?: ApiResponse()
                    body.resultCode = response.code()
                    onSuccess.invoke(body)
                }

                override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                    onError.invoke(t.toString())
                }
            })

        } else {
            onError.invoke("Bad request")
        }
    }
}