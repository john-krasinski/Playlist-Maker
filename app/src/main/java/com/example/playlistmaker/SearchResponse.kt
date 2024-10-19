package com.example.playlistmaker

data class SearchResponse(
    val resultCount: Int,
    val results: ArrayList<ResponseTrackInfo>
)

data class ResponseTrackInfo(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val collectionName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val country: String,
    val primaryGenreName: String,
    val releaseDate: String
)