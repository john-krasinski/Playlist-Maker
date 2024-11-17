package com.example.playlistmaker.data

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.domain.api.TracksHistoryRepository
import com.example.playlistmaker.domain.models.Track

class LocalTracksHistoryRepositoryImpl(context: Context): TracksHistoryRepository {

    private val history = SearchHistory(context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE))

    override fun getTracks(): List<Track> {
        return history.get().map {
            Track.from(it)
        }
    }

    override fun forgetTrack(track: Track) {
        history.remove(track.into())
    }

    override fun addTrack(track: Track) {
        history.add(track.into())
    }

    override fun clear() {
        history.clear()
    }
}