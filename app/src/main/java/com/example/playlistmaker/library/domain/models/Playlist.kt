package com.example.playlistmaker.library.domain.models

import com.example.playlistmaker.library.data.db.playlists.PlaylistEntity
import com.example.playlistmaker.search.domain.models.Track

data class Playlist(
    val id: Int = 0,
    var playlistName: String,
    var coverPath: String,
    var description: String,
    var trackIDs: List<Int> = listOf(),
    var numTracks: Int = trackIDs.size
) {
    fun intoDB() = PlaylistEntity(
        id = id,
        playlistName = playlistName,
        description = description,
        coverPath = coverPath,
        trackIDs = trackIDs.joinToString(",")
    )

    fun addTrack(track: Track) {
        val newList = trackIDs.toMutableList()
        newList.add(track.trackId)
        trackIDs = newList
        numTracks += 1
    }

    fun removeTrack(track: Track) {
        val newList = trackIDs.toMutableList()
        if (newList.remove(track.trackId)) {
            trackIDs = newList
            numTracks -= 1
        }
    }


    companion object {
        fun fromDB(entity: PlaylistEntity): Playlist {
            val trackIDs = if (entity.trackIDs.isNotEmpty()) {
                entity.trackIDs
                    .split(",")
                    .map {
                        try {
                            it.toInt()
                        } catch (e: Exception) {
                            null
                        }
                    }
                    .filterNotNull()
            } else {
                listOf()
            }

            return Playlist(
                id = entity.id,
                playlistName = entity.playlistName,
                coverPath = entity.coverPath,
                description = entity.description,
                trackIDs = trackIDs
            )
        }
    }
}