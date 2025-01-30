package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.search.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.tracks.TrackSearchState

class TracksViewModel(
    private val tracksInteractor: TracksInteractor,
    private val historyInteractor: TracksHistoryInteractor
): ViewModel() {

    private val _state = MutableLiveData<TrackSearchState>()
    private var history: List<Track> = listOf()
    init {
        loadHistory()
        _state.value = TrackSearchState.Initial(history)
    }

    private fun loadHistory() {
        history = historyInteractor.getTracks()
    }

    fun addToHistory(track:Track) {
        history = historyInteractor.addTrack(track)
    }

    fun clearHistory() {
        history = historyInteractor.clear()
    }

    fun search(query: String) {

        if (query.isNotEmpty()) {
            _state.postValue(TrackSearchState.Loading)

            tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer {

                override fun onSuccess(foundTracks: List<Track>) {

                    if (foundTracks.isEmpty()) {
                        _state.postValue(TrackSearchState.NotFound)
                    } else {
                        _state.postValue(TrackSearchState.Content(foundTracks))
                    }
                }

                override fun onError(message: String) {
                    _state.postValue(TrackSearchState.Error(message))
                }
            })
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
            historyInteractor: TracksHistoryInteractor
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    TracksViewModel(tracksInteractor, historyInteractor)
                }
            }
        }
    }
}