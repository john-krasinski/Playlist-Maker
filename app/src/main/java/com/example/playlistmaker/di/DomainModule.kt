package com.example.playlistmaker.di

import com.example.playlistmaker.library.domain.db.FavTracksInteractor
import com.example.playlistmaker.library.domain.impl.FavTracksInteractorImpl
import com.example.playlistmaker.search.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.impl.TracksHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.api.SharingProvider
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val domainModule = module {

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    factory<TracksHistoryInteractor> {
        TracksHistoryInteractorImpl(get())
    }

    single<FavTracksInteractor> {
        FavTracksInteractorImpl(get())
    }

}

