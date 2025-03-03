package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavTracksRepository {

    suspend fun insertTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    fun getAllTracks(): Flow<List<Track>>

    fun getTrackIds(): Flow<List<Int>>

}