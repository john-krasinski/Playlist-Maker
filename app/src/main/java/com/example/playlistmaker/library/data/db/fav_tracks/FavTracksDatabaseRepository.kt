package com.example.playlistmaker.library.data.db.fav_tracks

import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.domain.db.FavTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavTracksDatabaseRepository(
    private val database: AppDatabase
): FavTracksRepository {

    override suspend fun insertTrack(track: Track) {
        database.favouriteTrackDao().insertTrack(track.intoDB())
    }

    override suspend fun deleteTrack(track: Track) {
        database.favouriteTrackDao().deleteTrack(track.intoDB())
    }

    override fun getAllTracks(): Flow<List<Track>> = flow {
        val tracks = database.favouriteTrackDao().getAllTracks().toMutableList()
        tracks.sortWith { t1, t2 -> t2.timestamp.compareTo(t1.timestamp) } // sort descending by time added
        emit(tracks.map { Track.from(it) })
    }

    override fun getTrackIds(): Flow<List<Int>> = flow {
        emit(database.favouriteTrackDao().getTrackIds())
    }

}