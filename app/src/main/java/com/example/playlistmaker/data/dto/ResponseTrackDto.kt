package com.example.playlistmaker.data.dto

data class ResponseTrackDto(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val collectionName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val country: String,
    val primaryGenreName: String,
    val releaseDate: String,
    val previewUrl: String
)