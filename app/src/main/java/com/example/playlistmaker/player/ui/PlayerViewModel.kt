package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
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

enum class PlayerState {
    Default, Prepared, Playing, Paused, Completed
}

class PlayerViewModel(
    val track: Track,
    private val favTracksInteractor: FavTracksInteractor
): ViewModel() {

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default)
    fun curState(): LiveData<PlayerState> = playerState

    private val _isFavourite = MutableLiveData<Boolean>(track.isFavourite)
    val isFavourite:LiveData<Boolean> get() = _isFavourite

    private val mediaPlayer = MediaPlayer()

    init {
        preparePlayer(track.previewUrl)
    }

    fun processLikeClick() {
        if (track.isFavourite) {
            viewModelScope.launch(Dispatchers.IO) {
                favTracksInteractor.deleteTrack(track)
                _isFavourite.postValue(false)
                track.isFavourite = false
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                favTracksInteractor.insertTrack(track)
                _isFavourite.postValue(true)
                track.isFavourite = true
            }
        }
    }

    private fun preparePlayer(sourceUrl: String) {
        mediaPlayer.setDataSource(sourceUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState.postValue(PlayerState.Prepared)
        }
        mediaPlayer.setOnCompletionListener {
            playerState.postValue(PlayerState.Prepared)
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        playerState.postValue(PlayerState.Playing)
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playerState.postValue(PlayerState.Paused)
    }

    fun playbackControl() {
        when(playerState.value) {
            PlayerState.Playing -> {
                pausePlayer()
            }
            PlayerState.Prepared, PlayerState.Paused -> {
                startPlayer()
            }
            else -> {}
        }
    }

    fun position() = mediaPlayer.currentPosition

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }

    companion object {
        fun factory(
            track: Track,
            favTracksInteractor: FavTracksInteractor
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    PlayerViewModel(track,favTracksInteractor)
                }
            }
        }
    }
}