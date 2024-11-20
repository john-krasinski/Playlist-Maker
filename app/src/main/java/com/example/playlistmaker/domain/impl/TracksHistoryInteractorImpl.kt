package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.domain.api.TracksHistoryRepository
import com.example.playlistmaker.domain.models.Track

class TracksHistoryInteractorImpl(private val historyRepository: TracksHistoryRepository): TracksHistoryInteractor {
    override fun getTracks(): List<Track> {
        return historyRepository.getTracks()
    }

    override fun forgetTrack(track: Track) {
        return historyRepository.forgetTrack(track)
    }

    override fun addTrack(track: Track) {
        return historyRepository.addTrack(track)
    }

    override fun clear() {
        return historyRepository.clear()
    }
}