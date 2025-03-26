package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.db.PlaylistsInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.domain.models.PlaylistFullInfo
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val repository: PlaylistRepository):PlaylistsInteractor {
    override suspend fun createPlaylist(playlist: Playlist): Long {
        return repository.createPlaylist(playlist)
    }

    override suspend fun updatePlaylistInfo(playlist: Playlist): Int {
        return repository.updatePlaylistInfo(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun allPlaylistsFullInfo(): Flow<List<PlaylistFullInfo>> {
        return repository.allPlaylistsFullInfo()
    }

    override suspend fun getFullInfoPlaylist(id: Int): PlaylistFullInfo {
        return repository.getFullInfoPlaylist(id)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Int) {
        repository.addTrackToPlaylist(track, playlistId)
    }

    override suspend fun removeTrackFromPlaylist(trackId: Int, playlistId: Int) {
        repository.removeTrackFromPlaylist(trackId, playlistId)
    }
}