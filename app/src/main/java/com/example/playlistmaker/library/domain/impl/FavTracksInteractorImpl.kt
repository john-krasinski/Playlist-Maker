package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.db.FavTracksInteractor
import com.example.playlistmaker.library.domain.db.FavTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavTracksInteractorImpl(
    private val repository: FavTracksRepository
): FavTracksInteractor {
    override suspend fun insertTrack(track: Track) {
        return repository.insertTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        return repository.deleteTrack(track)
    }

    override fun getAllTracks(): Flow<List<Track>> {
        return repository.getAllTracks()
    }

    override fun getTrackIds(): Flow<List<Int>> {
        return repository.getTrackIds()
    }

}