package com.example.playlistmaker.search.data.dto

data class LocalHistoryTrackDto(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val albumName: String,
    val trackTime: String,
    val artworkUrl: String,
    val country: String,
    val genre: String,
    val year: String,
    val previewUrl: String
)