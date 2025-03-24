package com.example.playlistmaker.library.data.db.playlists

import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.domain.models.PlaylistFullInfo
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistDatabaseRepository(private val database: AppDatabase): PlaylistRepository {
    override suspend fun createPlaylist(playlist: Playlist): Long {
        return database.playlistsDao().createPlaylist(playlist.intoDB())
    }

    override suspend fun updatePlaylistInfo(playlist: Playlist): Int {
        return database.playlistsDao().updatePlaylist(playlist.intoDB())
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        database.playlistsDao().deletePlaylistById(playlist.id)
        val trackIds = getTrackIdsForPlaylist(playlist.id)
        for (trackId in trackIds) {
            removeTrackFromPlaylist(trackId, playlist.id)
        }
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = database.playlistsDao().getPlaylists().map { Playlist.fromDB(it) }
        for (playlist in playlists) {
            playlist.trackIDs = getTrackIdsForPlaylist(playlist.id).toMutableList()
        }
        emit(playlists)
    }


    override suspend fun allPlaylistsFullInfo(): Flow<List<PlaylistFullInfo>> = flow {
        emit(database.playlistsDao().getPlaylistsDetails().map { PlaylistFullInfo.fromDB(it) })
    }

    override suspend fun getFullInfoPlaylist(id: Int): PlaylistFullInfo {
        val info = database.playlistsDao().getDetailsForPlaylist(id)
        return PlaylistFullInfo.fromDB(info)
    }


    override suspend fun addTrackToPlaylist(track: Track, playlistId: Int) {
        database.playlistsDao().insertTrack(track.intoPlaylistedDB())
        database.playlistsDao().linkTrackToPlaylist(
            PlaylistTrackCrossRefEntity(
                playlistId = playlistId,
                trackId = track.trackId
            )
        )
    }

    override suspend fun removeTrackFromPlaylist(trackId: Int, playlistId: Int) {
        database.playlistsDao().unlinkTrackFromPlaylist(
            PlaylistTrackCrossRefEntity(
                playlistId = playlistId,
                trackId = trackId
            )
        )
        val otherLinks = database.playlistsDao().getPlaylistIdsForTrack(trackId)
        if (otherLinks.isEmpty()) {
            database.playlistsDao().deleteTrackById(trackId)
        }
    }

    private suspend fun getTrackIdsForPlaylist(playlistId: Int): List<Int> {
        return database.playlistsDao().getTrackIdsForPlaylist(playlistId)
    }
}