package com.example.playlistmaker.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.PlaylistsInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistsInteractor: PlaylistsInteractor): ViewModel() {

    private val _playlists = MutableLiveData<List<Playlist>>(listOf())
    val playlists: LiveData<List<Playlist>> get() = _playlists

    fun createPlaylist(name: String, description: String, cover: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.createPlaylist(Playlist(
                playlistName = name,
                description = description,
                coverPath = cover,
                trackIDs = listOf()
            ))
        }
    }

    fun updatePlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getPlaylists().collect {
                _playlists.postValue(it)
            }
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.deletePlaylist(playlist)
        }
    }
}