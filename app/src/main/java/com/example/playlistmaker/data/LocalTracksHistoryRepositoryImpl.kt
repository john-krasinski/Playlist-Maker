package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.TracksHistoryRepository
import com.example.playlistmaker.domain.models.Track

class LocalTracksHistoryRepositoryImpl(sharedPreferences: SharedPreferences): TracksHistoryRepository {

    private val history = SearchHistory(sharedPreferences)

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