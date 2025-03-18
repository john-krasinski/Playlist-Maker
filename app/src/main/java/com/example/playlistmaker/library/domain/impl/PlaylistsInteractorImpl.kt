package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.db.PlaylistsInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val repository: PlaylistRepository):PlaylistsInteractor {
    override suspend fun createPlaylist(playlist: Playlist): Long {
        return repository.createPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) : Int {
        return repository.updatePlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }
}