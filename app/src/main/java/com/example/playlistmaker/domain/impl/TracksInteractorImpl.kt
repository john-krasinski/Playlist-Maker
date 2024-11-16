package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

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