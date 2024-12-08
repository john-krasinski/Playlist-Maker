package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.dto.ApiResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.data.ApiClient

class TracksRepositoryImpl(private val apiClient: ApiClient): TracksRepository {

    override fun searchTracks(query: String, onSuccess: (List<Track>) -> Unit, onError: (String) -> Unit) {

        val onApiSuccess = { resp: ApiResponse ->
            val foundTracks = resp as TrackSearchResponse
            if (foundTracks.resultCount > 0 && foundTracks.results.isNotEmpty()) {
                val tracks = foundTracks.results.map {
                    Track.from(it)
                }
                onSuccess.invoke(tracks)
            } else {
                onSuccess.invoke(emptyList())
            }
        }

        apiClient.doRequest(TrackSearchRequest(query), onApiSuccess, onError)
    }
}