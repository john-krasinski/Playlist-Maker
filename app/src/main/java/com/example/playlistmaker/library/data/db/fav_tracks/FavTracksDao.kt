package com.example.playlistmaker.library.data.db.fav_tracks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavTrackDao {
    @Insert(entity = FavTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavTrackEntity)

    @Delete(entity = FavTrackEntity::class)
    suspend fun deleteTrack(track: FavTrackEntity)

    @Query("SELECT * FROM $FAV_TRACKS_TABLE_NAME ")
    suspend fun getAllTracks(): List<FavTrackEntity>

    @Query("SELECT trackId from $FAV_TRACKS_TABLE_NAME ")
    suspend fun getTrackIds(): List<Int>
}