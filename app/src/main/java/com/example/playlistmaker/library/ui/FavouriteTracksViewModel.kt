package com.example.playlistmaker.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.library.domain.db.FavTracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed interface FavTracksLoadingState {
    object Loading: FavTracksLoadingState
    object Empty: FavTracksLoadingState
    data class Content(val tracks: List<Track>): FavTracksLoadingState
}

class FavouriteTracksViewModel(
    val favTracksInteractor: FavTracksInteractor
) : ViewModel() {

    private val _favTracks = MutableLiveData<FavTracksLoadingState>(FavTracksLoadingState.Loading)
    val favTracks: LiveData<FavTracksLoadingState> get() = _favTracks

    init {
        updateFavTracks()
    }

    fun updateFavTracks() {
        _favTracks.value = FavTracksLoadingState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            favTracksInteractor.getAllTracks().collect {
                if (it.isEmpty()) {
                    _favTracks.postValue(FavTracksLoadingState.Empty)
                } else {
                    _favTracks.postValue(FavTracksLoadingState.Content(it))
                }
            }
        }
    }


    companion object {
        fun factory(favTracksInteractor: FavTracksInteractor): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    FavouriteTracksViewModel(favTracksInteractor)
                }
            }
        }
    }
}