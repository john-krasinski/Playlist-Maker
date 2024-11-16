package com.example.playlistmaker.domain.models

data class Track(
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