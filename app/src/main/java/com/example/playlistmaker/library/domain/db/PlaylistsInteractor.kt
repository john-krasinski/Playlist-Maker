package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun createPlaylist(playlist: Playlist): Long
    suspend fun updatePlaylist(playlist: Playlist): Int
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun getPlaylists(): Flow<List<Playlist>>
}