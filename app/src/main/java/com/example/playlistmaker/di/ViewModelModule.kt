package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.App
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.TracksViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        TracksViewModel(get(), get())
    }

    viewModel { (track: Track) ->
        PlayerViewModel(track)
    }

    viewModel { (application: App) ->
        SettingsViewModel(application, get())
    }
}