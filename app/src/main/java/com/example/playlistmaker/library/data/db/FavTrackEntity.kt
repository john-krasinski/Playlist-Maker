package com.example.playlistmaker.library.data.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import java.sql.Timestamp

const val FAV_TRACKS_TABLE_NAME = "fav_tracks_table"
const val DB_NAME = "playlist_maker.db"

@Entity(tableName = FAV_TRACKS_TABLE_NAME)
data class FavTrackEntity(
    @PrimaryKey(autoGenerate = false)
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val albumName: String,
    val trackTime: String,
    val artworkUrl: String,
    val country: String,
    val genre: String,
    val year: String,
    val previewUrl: String,
    val timestamp: Long
)


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


@Database(version = 2, entities = [FavTrackEntity::class])
abstract class FavTracksDB: RoomDatabase() {
    abstract fun favouriteTrackDao(): FavTrackDao
}