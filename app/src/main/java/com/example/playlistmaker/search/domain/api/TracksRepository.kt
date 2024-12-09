package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface TracksRepository {
    fun searchTracks(query: String, onSuccess: (List<Track>) -> Unit, onError: (String) -> Unit)
}