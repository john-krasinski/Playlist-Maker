package com.example.playlistmaker.di

import com.example.playlistmaker.App
import com.example.playlistmaker.library.ui.FavouriteTracksViewModel
import com.example.playlistmaker.library.ui.PlaylistsViewModel
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchTracksViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchTracksViewModel(get(), get(), get())
    }

    viewModel { (track: Track) ->
        PlayerViewModel(track, get(), get())
    }

    viewModel { (application: App) ->
        SettingsViewModel(application, get())
    }

    viewModel {
        FavouriteTracksViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }
}