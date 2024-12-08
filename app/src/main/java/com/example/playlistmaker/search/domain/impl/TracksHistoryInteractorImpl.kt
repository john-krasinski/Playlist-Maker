package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksHistoryRepository
import com.example.playlistmaker.search.domain.models.Track

class TracksHistoryInteractorImpl(private val historyRepository: TracksHistoryRepository):
    TracksHistoryInteractor {
    override fun getTracks(): List<Track> {
        return historyRepository.getTracks()
    }

    override fun forgetTrack(track: Track): List<Track> {
        return historyRepository.forgetTrack(track)
    }

    override fun addTrack(track: Track): List<Track> {
        return historyRepository.addTrack(track)
    }

    override fun clear(): List<Track> {
        return historyRepository.clear()
    }
}