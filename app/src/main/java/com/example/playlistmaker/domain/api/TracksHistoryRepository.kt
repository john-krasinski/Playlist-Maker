package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TracksHistoryRepository {
    fun getTracks(): List<Track>
    fun forgetTrack(track: Track)
    fun addTrack(track: Track)
    fun clear()
}