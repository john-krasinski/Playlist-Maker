package com.example.playlistmaker.search.ui.tracks

import com.example.playlistmaker.search.domain.models.Track

sealed interface TrackSearchState {
    data class Initial(val historyTracks: List<Track>): TrackSearchState
    object Loading: TrackSearchState
    data class Error(val message: String) :TrackSearchState
    object NotFound: TrackSearchState
    data class Content(val foundTracks: List<Track>): TrackSearchState
}
