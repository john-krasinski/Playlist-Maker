package com.example.playlistmaker.search.data.impl

import android.content.Context
import com.example.playlistmaker.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.data.SearchHistory
import com.example.playlistmaker.search.domain.api.TracksHistoryRepository

class LocalTracksHistoryRepositoryImpl(context: Context): TracksHistoryRepository {

    private val history = SearchHistory(context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE))

    override fun getTracks(): List<Track> {
        return history.get().map {
            Track.from(it)
        }
    }

    override fun forgetTrack(track: Track): List<Track>{
        history.remove(track.into())
        return getTracks()
    }

    override fun addTrack(track: Track): List<Track> {
        history.add(track.into())
        return getTracks()
    }

    override fun clear(): List<Track> {
        history.clear()
        return emptyList()
    }
}