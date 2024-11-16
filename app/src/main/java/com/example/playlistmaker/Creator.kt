package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.data.LocalTracksHistoryRepositoryImpl
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitApiClient
import com.example.playlistmaker.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.domain.api.TracksHistoryRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.TracksHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitApiClient())
    }

    private fun getHistoryRepository(sharedPreferences: SharedPreferences): TracksHistoryRepository {
        return LocalTracksHistoryRepositoryImpl(sharedPreferences)
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideHistoryInteractor(sharedPreferences: SharedPreferences): TracksHistoryInteractor {
        return TracksHistoryInteractorImpl(getHistoryRepository(sharedPreferences))
    }
}