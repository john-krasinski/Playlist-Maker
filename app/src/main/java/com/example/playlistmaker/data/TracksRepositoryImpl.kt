package com.example.playlistmaker.data

import android.icu.text.SimpleDateFormat
import com.example.playlistmaker.data.dto.ApiResponse
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track
import java.util.Locale

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