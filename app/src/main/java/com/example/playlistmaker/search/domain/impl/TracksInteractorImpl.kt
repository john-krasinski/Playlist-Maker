package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.ResourceFoundTracks
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl(private val tracksRepository: TracksRepository): TracksInteractor {

    override fun searchTracks(query: String): Flow<ResourceFoundTracks> {

        return tracksRepository.searchTracks(query)
    }
}