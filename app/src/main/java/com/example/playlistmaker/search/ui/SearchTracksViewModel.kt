package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.library.domain.db.FavTracksInteractor
import com.example.playlistmaker.search.domain.api.ResourceFoundTracks
import com.example.playlistmaker.search.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.tracks.TrackSearchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchTracksViewModel(
    private val tracksInteractor: TracksInteractor,
    private val historyInteractor: TracksHistoryInteractor,
    private val favTracksInteractor: FavTracksInteractor
): ViewModel() {

    private val _state = MutableLiveData<TrackSearchState>()
    private var history: List<Track> = listOf()
    private var favTracksIDs: List<Int> = listOf()

    init {
        refreshViewModel()
        _state.value = TrackSearchState.Initial(history)
    }

    private fun loadHistory() {
        history = historyInteractor.getTracks()
//        markFavTracks(history)
    }

    fun addToHistory(track:Track) {
        history = historyInteractor.addTrack(track)
    }

    fun clearHistory() {
        history = historyInteractor.clear()
    }

    fun refreshViewModel() {
        getFavTracks()
        loadHistory()
    }

    fun isTrackFavourite(track: Track) = favTracksIDs.contains(track.trackId)
//    private fun markFavTracks(tracks:List<Track>) {
//        tracks.forEach {
//            if (favTracksIDs.contains(it.trackId)) {
//                it.isFavourite = true
//            }
//        }
//    }

    fun getFavTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            favTracksInteractor.getTrackIds().collect { favTracksIDs = it }
        }
    }

    fun search(query: String) {

        if (query.isNotEmpty()) {
            _state.postValue(TrackSearchState.Loading)

            viewModelScope.launch {
                tracksInteractor.searchTracks(query).collect { resourse ->
                    when (resourse) {
                        is ResourceFoundTracks.Error -> _state.postValue(TrackSearchState.Error(resourse.message))
                        ResourceFoundTracks.NotFound -> _state.postValue(TrackSearchState.NotFound)
                        is ResourceFoundTracks.Content -> {
//                            markFavTracks(resourse.foundTracks)
                            _state.postValue(TrackSearchState.Content(resourse.foundTracks))
                        }
                    }
                }
            }
        }
    }

    fun resetSearch() {
        _state.value = TrackSearchState.Initial(history)
    }

    val state : LiveData<TrackSearchState>
        get() = _state


    companion object {
        fun factory(
            tracksInteractor: TracksInteractor,
            historyInteractor: TracksHistoryInteractor,
            favTracksInteractor: FavTracksInteractor
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SearchTracksViewModel(tracksInteractor, historyInteractor, favTracksInteractor)
                }
            }
        }
    }
}