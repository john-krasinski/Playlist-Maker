package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

sealed interface ResourceFoundTracks {
    data class Content(val foundTracks: List<Track>): ResourceFoundTracks
    data class Error(val message: String): ResourceFoundTracks
    object NotFound: ResourceFoundTracks
}