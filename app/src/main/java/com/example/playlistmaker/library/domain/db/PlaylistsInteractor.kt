package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.domain.models.PlaylistFullInfo
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun createPlaylist(playlist: Playlist): Long
    suspend fun updatePlaylistInfo(playlist: Playlist): Int
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun allPlaylistsFullInfo(): Flow<List<PlaylistFullInfo>>
    suspend fun getFullInfoPlaylist(id: Int): PlaylistFullInfo

    suspend fun addTrackToPlaylist(track: Track, playlistId: Int)
    suspend fun removeTrackFromPlaylist(trackId: Int, playlistId: Int)
}