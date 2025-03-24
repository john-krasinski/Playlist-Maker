package com.example.playlistmaker.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.FavTracksInteractor
import com.example.playlistmaker.library.domain.db.PlaylistsInteractor
import com.example.playlistmaker.library.domain.models.EMPTY_PLAYLIST_INFO
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.domain.models.PlaylistFullInfo
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.sharing.domain.api.SharingProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManagePlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val sharingProvider: SharingProvider
) : ViewModel() {

    private var _playlist = MutableLiveData<PlaylistFullInfo?>(EMPTY_PLAYLIST_INFO)
    val playlist: LiveData<PlaylistFullInfo?> get() = _playlist

    fun getPlaylistFullInfo(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val info = playlistsInteractor.getFullInfoPlaylist(playlistId)
                _playlist.postValue(info)
            } catch (e: Exception) {
                _playlist.postValue(null)
            }
        }
    }

    fun sharePlaylist(message: String) {
        sharingProvider.sharePlaylist(message)
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.deletePlaylist(playlist)
            _playlist.postValue(null)
        }
    }

    fun removeTrackFromPlaylist(track: Track, playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.removeTrackFromPlaylist(
                trackId = track.trackId,
                playlistId = playlistId
            )
        }
    }
}