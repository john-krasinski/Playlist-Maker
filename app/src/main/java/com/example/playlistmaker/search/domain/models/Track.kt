package com.example.playlistmaker.search.domain.models

import android.icu.text.SimpleDateFormat
import com.example.playlistmaker.library.data.db.fav_tracks.FavTrackEntity
import com.example.playlistmaker.search.data.dto.LocalHistoryTrackDto
import com.example.playlistmaker.search.data.dto.ResponseTrackDto
import java.util.Locale

data class Track(
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
    var isFavourite: Boolean = false
) {

    companion object {
        
        fun from(src: ResponseTrackDto): Track {
            return Track(
                trackId = src.trackId,
                trackName = src.trackName,
                artistName = src.artistName,
                albumName = src.collectionName,
                trackTime = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(src.trackTimeMillis),
                artworkUrl = src.artworkUrl100,
                country = src.country,
                genre = src.primaryGenreName,
                year = src.releaseDate.replaceAfter('-', ""),
                previewUrl = src.previewUrl
            )
        }

        fun from(src: LocalHistoryTrackDto): Track {
            return Track(
                trackId = src.trackId,
                trackName = src.trackName,
                trackTime = src.trackTime,
                artistName = src.artistName,
                year = src.year,
                artworkUrl = src.artworkUrl,
                country = src.country,
                previewUrl = src.previewUrl,
                albumName = src.albumName,
                genre = src.genre
            )
        }

        fun from(src: FavTrackEntity): Track {
            return Track(
                trackId = src.trackId,
                trackName = src.trackName,
                trackTime = src.trackTime,
                artistName = src.artistName,
                year = src.year,
                artworkUrl = src.artworkUrl,
                country = src.country,
                previewUrl = src.previewUrl,
                albumName = src.albumName,
                genre = src.genre,
                isFavourite = true
            )
        }
    }

    fun intoDB(): FavTrackEntity {
        return FavTrackEntity(
            trackId = this.trackId,
            trackName = this.trackName,
            trackTime = this.trackTime,
            artistName = this.artistName,
            year = this.year,
            artworkUrl = this.artworkUrl,
            country = this.country,
            previewUrl = this.previewUrl,
            albumName = this.albumName,
            genre = this.genre,
            timestamp = System.currentTimeMillis()
        )
    }

    fun intoHistory(): LocalHistoryTrackDto {
        return LocalHistoryTrackDto(
            trackId = this.trackId,
            trackName = this.trackName,
            trackTime = this.trackTime,
            artistName = this.artistName,
            year = this.year,
            artworkUrl = this.artworkUrl,
            country = this.country,
            previewUrl = this.previewUrl,
            albumName = this.albumName,
            genre = this.genre)
    }

}