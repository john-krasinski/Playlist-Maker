package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.data.SearchHistory
import com.example.playlistmaker.search.domain.api.TracksHistoryRepository

class LocalTracksHistoryRepositoryImpl(private val history: SearchHistory): TracksHistoryRepository {

    override fun getTracks(): List<Track> {
        return history.get().map {
            Track.from(it)
        }
    }

    override fun forgetTrack(track: Track): List<Track>{
        history.remove(track.intoHistory())
        return getTracks()
    }

    override fun addTrack(track: Track): List<Track> {
        history.add(track.intoHistory())
        return getTracks()
    }

    override fun clear(): List<Track> {
        history.clear()
        return emptyList()
    }
}