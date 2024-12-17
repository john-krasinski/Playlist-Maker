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

    private val state = MutableLiveData<TrackSearchState>()

    private val history = MutableLiveData<List<Track>>()
    init {
        loadHistory()
        state.postValue(TrackSearchState.Initial)
    }

    private fun loadHistory() {
        history.postValue(historyInteractor.getTracks())
    }

    fun addToHistory(track:Track) {
        history.postValue(historyInteractor.addTrack(track))
    }

    fun clearHistory() {
        history.postValue(historyInteractor.clear())
    }

    fun search(query: String) {

        if (query.isNotEmpty()) {
            state.postValue(TrackSearchState.Loading)

            tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer {

                override fun onSuccess(foundTracks: List<Track>) {

                    if (foundTracks.isEmpty()) {
                        state.postValue(TrackSearchState.NotFound)
                    } else {
                        state.postValue(TrackSearchState.Content(foundTracks))
                    }
                }

                override fun onError(message: String) {
                    state.postValue(TrackSearchState.Error(message))
                }
            })
        }
    }

    fun getState() : LiveData<TrackSearchState> = state

    fun getHistory(): LiveData<List<Track>> = history

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