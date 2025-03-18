package com.example.playlistmaker.library.data.db.playlists

import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistDatabaseRepository(private val database: AppDatabase): PlaylistRepository {
    override suspend fun createPlaylist(playlist: Playlist): Long {
        return database.playlistsDao().createPlaylist(playlist.intoDB())
    }

    override suspend fun updatePlaylist(playlist: Playlist): Int {
        return database.playlistsDao().updatePlaylist(playlist.intoDB())
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        database.playlistsDao().deletePlaylist(playlist.intoDB())
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        emit(database.playlistsDao().getPlaylists().map { Playlist.fromDB(it) })
    }
}