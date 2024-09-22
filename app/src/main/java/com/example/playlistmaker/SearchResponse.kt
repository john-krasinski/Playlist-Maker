package com.example.playlistmaker

data class SearchResponse(
    val resultCount: Int,
    val results: ArrayList<ResponseTrackInfo>
)

data class ResponseTrackInfo(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String
)