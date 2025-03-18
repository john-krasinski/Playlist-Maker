package com.example.playlistmaker.library.data.db.fav_tracks

import androidx.room.Entity
import androidx.room.PrimaryKey

const val FAV_TRACKS_TABLE_NAME = "fav_tracks_table"

@Entity(tableName = FAV_TRACKS_TABLE_NAME)
data class FavTrackEntity(
    @PrimaryKey(autoGenerate = false)
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val albumName: String,
    val trackTime: String,
    val artworkUrl: String,
    val country: String,
    val genre: String,
    val year: String,
    val previewUrl: String,
    val timestamp: Long
)

