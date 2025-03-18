package com.example.playlistmaker.library.data.db.playlists
import androidx.room.Entity
import androidx.room.PrimaryKey

const val PLAYLISTS_TABLE_NAME = "playlists_table"

@Entity(tableName = PLAYLISTS_TABLE_NAME)
class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val playlistName: String,
    val description: String,
    val coverPath: String,
    val trackIDs: String
)
