package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.dto.ApiResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.data.ApiClient
import com.example.playlistmaker.search.domain.api.ResourceFoundTracks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val apiClient: ApiClient): TracksRepository {

    override fun searchTracks(query: String): Flow<ResourceFoundTracks> = flow {
        val response = apiClient.doRequest(TrackSearchRequest(query))
        when (response.resultCode) {
            200 -> {
                with (response as TrackSearchResponse) {
                    if (response.resultCount > 0 && response.results.isNotEmpty()) {
                        val tracks = response.results.map { Track.from(it) }
                        emit(ResourceFoundTracks.Content(tracks))
                    } else {
                        emit(ResourceFoundTracks.NotFound)
                    }
                }
            } else -> emit(ResourceFoundTracks.Error("Ошибка сервера"))
        }
    }
}