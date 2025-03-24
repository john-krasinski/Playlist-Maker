package com.example.playlistmaker.library.data.db.playlists

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

const val PLAYLISTS_WITH_TRACKS_TABLE = "playlists_with_tracks"

@Entity(
    tableName = PLAYLISTS_WITH_TRACKS_TABLE,
    primaryKeys = ["playlistId", "trackId"]
)
data class PlaylistTrackCrossRefEntity(
    val playlistId: Int,
    val trackId: Int
)


data class PlaylistDetails(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "trackId",
        entity = PlaylistedTrackEntity::class,
        associateBy = Junction(
            PlaylistTrackCrossRefEntity::class
        )
    ) val tracks: List<PlaylistedTrackEntity>
)
