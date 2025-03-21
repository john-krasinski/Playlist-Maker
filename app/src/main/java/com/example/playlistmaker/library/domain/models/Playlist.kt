package com.example.playlistmaker.library.domain.models

import com.example.playlistmaker.library.data.db.playlists.PlaylistEntity
import com.example.playlistmaker.library.data.db.playlists.PlaylistDetails
import com.example.playlistmaker.search.domain.models.Track

data class Playlist(
    val id: Int = 0,
    var playlistName: String,
    var coverPath: String,
    var description: String,
    var trackIDs: MutableList<Int> = mutableListOf()
) {

    val numTracks get() = trackIDs.size

    fun intoDB() = PlaylistEntity(
        playlistId = id,
        playlistName = playlistName,
        description = description,
        coverPath = coverPath,
//        trackIDs = trackIDs.joinToString(",")
    )

    fun addTrack(track: Track) {
        trackIDs.add(track.trackId)
    }

    fun removeTrack(track: Track) {
        trackIDs.remove(track.trackId)
    }

    companion object {
        fun fromDB(entity: PlaylistEntity): Playlist {

            return Playlist(
                id = entity.playlistId,
                playlistName = entity.playlistName,
                coverPath = entity.coverPath,
                description = entity.description,
//                trackIDs = trackIDs
            )
        }
    }
}

val EMPTY_PLAYLIST_INFO = PlaylistFullInfo(0, "","","", listOf())

data class PlaylistFullInfo(
    val id: Int = 0,
    var playlistName: String,
    var coverPath: String,
    var description: String,
    var tracks: List<Track>
) {


    fun addTrack(track: Track) {
        val newList = tracks.toMutableList()
        newList.add(track)
        tracks = newList
    }

    fun removeTrack(track: Track) {
        val newList = tracks.toMutableList()
        newList.remove(track)
        tracks = newList
    }

    companion object {
        fun fromDB(entity: PlaylistDetails): PlaylistFullInfo {

            return PlaylistFullInfo(
                id = entity.playlist.playlistId,
                playlistName = entity.playlist.playlistName,
                coverPath = entity.playlist.coverPath,
                description = entity.playlist.description,
                tracks = entity.tracks.map { Track.from(it) }
            )
        }
    }
}