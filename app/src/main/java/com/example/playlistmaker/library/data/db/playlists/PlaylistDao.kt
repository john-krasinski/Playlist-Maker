package com.example.playlistmaker.library.data.db.playlists

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlaylistDao {

    //  playlists
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun createPlaylist(playlist: PlaylistEntity): Long

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlist: PlaylistEntity): Int

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("DELETE FROM $PLAYLISTS_TABLE_NAME WHERE playlistId = :id")
    suspend fun deletePlaylistById(id: Int)

    @Query("SELECT * FROM $PLAYLISTS_TABLE_NAME ")
    suspend fun getPlaylists(): List<PlaylistEntity>


    //  tracks
    @Insert(entity = PlaylistedTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: PlaylistedTrackEntity)

    @Delete(entity = PlaylistedTrackEntity::class)
    suspend fun deleteTrack(track: PlaylistedTrackEntity)

    @Query("DELETE FROM $PLAYLISTED_TRACKS_TABLE_NAME WHERE trackId = :id")
    suspend fun deleteTrackById(id: Int)

    @Query("SELECT * FROM $PLAYLISTED_TRACKS_TABLE_NAME ")
    suspend fun getAllTracks(): List<PlaylistedTrackEntity>

    @Query("SELECT trackId FROM $PLAYLISTED_TRACKS_TABLE_NAME ")
    suspend fun getTrackIds(): List<Int>


    //  cross references
    @Insert(entity = PlaylistTrackCrossRefEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun linkTrackToPlaylist(record: PlaylistTrackCrossRefEntity)

    @Delete(entity = PlaylistTrackCrossRefEntity::class)
    suspend fun unlinkTrackFromPlaylist(record: PlaylistTrackCrossRefEntity)

    @Query("DELETE FROM $PLAYLISTS_WITH_TRACKS_TABLE WHERE playlistId = :id")
    suspend fun unlinkRemovedPlaylistTracks(id: Int)

    @Query("SELECT trackId FROM $PLAYLISTS_WITH_TRACKS_TABLE WHERE playlistId = :id")
    suspend fun getTrackIdsForPlaylist(id: Int): List<Int>

    @Query("SELECT playlistId FROM $PLAYLISTS_WITH_TRACKS_TABLE WHERE trackId = :trackId")
    suspend fun getPlaylistIdsForTrack(trackId: Int): List<Int>

    @Query("SELECT * FROM $PLAYLISTS_WITH_TRACKS_TABLE")
    suspend fun allLinksTrackToPlaylist(): List<PlaylistTrackCrossRefEntity>


    //  full playlist info
    @Query("SELECT * FROM $PLAYLISTS_TABLE_NAME")
    suspend fun getPlaylistsDetails(): List<PlaylistDetails>

    @Query("SELECT * FROM $PLAYLISTS_TABLE_NAME WHERE playlistId = :id")
    suspend fun getDetailsForPlaylist(id: Int): PlaylistDetails

}