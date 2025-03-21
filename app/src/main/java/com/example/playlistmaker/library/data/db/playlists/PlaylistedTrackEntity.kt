package com.example.playlistmaker.library.data.db.playlists

import androidx.room.Entity
import androidx.room.PrimaryKey

const val PLAYLISTED_TRACKS_TABLE_NAME = "playlisted_tracks"

@Entity(tableName = PLAYLISTED_TRACKS_TABLE_NAME)
data class PlaylistedTrackEntity(
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