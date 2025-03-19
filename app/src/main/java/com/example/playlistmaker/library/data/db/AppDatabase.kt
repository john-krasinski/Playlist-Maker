package com.example.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.library.data.db.fav_tracks.FavTrackDao
import com.example.playlistmaker.library.data.db.fav_tracks.FavTrackEntity
import com.example.playlistmaker.library.data.db.playlists.PlaylistDao
import com.example.playlistmaker.library.data.db.playlists.PlaylistEntity


const val DB_NAME = "playlist_maker.db"

@Database(version = 3, entities = [FavTrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun favouriteTrackDao(): FavTrackDao
    abstract fun playlistsDao(): PlaylistDao
}