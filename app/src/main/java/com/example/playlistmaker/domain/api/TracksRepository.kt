package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(query: String, onSuccess: (List<Track>) -> Unit, onError: (String) -> Unit)
}