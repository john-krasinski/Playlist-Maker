package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.AppRemoteActionsProviderImpl
import com.example.playlistmaker.data.AppSettingsProviderImpl
import com.example.playlistmaker.data.LocalTracksHistoryRepositoryImpl
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitApiClient
import com.example.playlistmaker.domain.api.AppRemoteActionsInteractor
import com.example.playlistmaker.domain.api.AppRemoteActionsProvider
import com.example.playlistmaker.domain.api.AppSettingsInteractor
import com.example.playlistmaker.domain.api.AppSettingsProvider
import com.example.playlistmaker.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.domain.api.TracksHistoryRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.AppRemoteActionsInteractorImpl
import com.example.playlistmaker.domain.impl.AppSettingsInteractorImpl
import com.example.playlistmaker.domain.impl.TracksHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitApiClient())
    }
    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getHistoryRepository(context: Context): TracksHistoryRepository {
        return LocalTracksHistoryRepositoryImpl(context)
    }
    fun provideHistoryInteractor(context: Context): TracksHistoryInteractor {
        return TracksHistoryInteractorImpl(getHistoryRepository(context))
    }

    private fun getAppSettingsProvider(context: Context): AppSettingsProvider {
        return AppSettingsProviderImpl(context)
    }
    fun provideAppSettingsInteractor(context: Context): AppSettingsInteractor {
        return AppSettingsInteractorImpl(getAppSettingsProvider(context))
    }

    private fun getAppRemoteActionsProvider(context: Context): AppRemoteActionsProvider {
        return AppRemoteActionsProviderImpl(context)
    }
    fun provideAppRemoteActionsInteractor(context: Context): AppRemoteActionsInteractor {
        return AppRemoteActionsInteractorImpl(getAppRemoteActionsProvider(context))
    }
}