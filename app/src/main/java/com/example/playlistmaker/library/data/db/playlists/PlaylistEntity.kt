package com.example.playlistmaker.library.data.db.playlists
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

const val PLAYLISTS_TABLE_NAME = "playlists"

@Entity(tableName = PLAYLISTS_TABLE_NAME)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
//    @ForeignKey(
//        entity =
//    )
    val playlistId: Int = 0,
    val playlistName: String,
    val description: String,
    val coverPath: String
)
