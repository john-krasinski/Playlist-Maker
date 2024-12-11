package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface TracksInteractor {

    fun searchTracks(query:String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun onSuccess(foundTracks: List<Track>)

        fun onError(message: String)
    }
}