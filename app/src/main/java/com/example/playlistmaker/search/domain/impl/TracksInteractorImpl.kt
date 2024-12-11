package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Track

class TracksInteractorImpl(private val tracksRepository: TracksRepository): TracksInteractor {

    override fun searchTracks(query: String, consumer: TracksInteractor.TracksConsumer) {

        val onSuccess = { tracks: List<Track> ->
            consumer.onSuccess(tracks)
        }

        val onError = { msg: String ->
            consumer.onError(msg)
        }

        tracksRepository.searchTracks(query, onSuccess, onError)
    }
}