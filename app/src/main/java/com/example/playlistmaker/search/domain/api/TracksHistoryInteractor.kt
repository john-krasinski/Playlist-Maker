package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface TracksHistoryInteractor {
    fun getTracks(): List<Track>
    fun forgetTrack(track: Track): List<Track>
    fun addTrack(track: Track): List<Track>
    fun clear(): List<Track>
}