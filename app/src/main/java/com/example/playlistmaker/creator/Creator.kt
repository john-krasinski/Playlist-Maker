package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.sharing.data.impl.SharingProviderImpl
import com.example.playlistmaker.settings.data.impl.SettingsProviderImpl
import com.example.playlistmaker.search.data.impl.LocalTracksHistoryRepositoryImpl
import com.example.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitApiClient
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.api.SharingProvider
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.api.SettingsProvider
import com.example.playlistmaker.search.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksHistoryRepository
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl

object Creator {

    private lateinit var appContext: Context
    fun setAppContext(context: Context) {
        appContext = context
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitApiClient())
    }
    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getHistoryRepository(context: Context): TracksHistoryRepository {
        return LocalTracksHistoryRepositoryImpl(context)
    }
    fun provideHistoryInteractor(): TracksHistoryInteractor {
        return TracksHistoryInteractorImpl(getHistoryRepository(appContext))
    }

    private fun getAppSettingsProvider(context: Context): SettingsProvider {
        return SettingsProviderImpl(context)
    }
    fun provideAppSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getAppSettingsProvider(appContext))
    }

    private fun getAppRemoteActionsProvider(context: Context): SharingProvider {
        return SharingProviderImpl(context)
    }
    fun provideAppRemoteActionsInteractor(): SharingInteractor {
        return SharingInteractorImpl(getAppRemoteActionsProvider(appContext))
    }
}