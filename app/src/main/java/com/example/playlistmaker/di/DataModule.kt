package com.example.playlistmaker.di

import android.app.Application.MODE_PRIVATE
import android.content.Context
import com.example.playlistmaker.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.search.data.ApiClient
import com.example.playlistmaker.search.data.SearchHistory
import com.example.playlistmaker.search.data.impl.LocalTracksHistoryRepositoryImpl
import com.example.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitApiClient
import com.example.playlistmaker.search.data.network.SearchApi
import com.example.playlistmaker.search.domain.api.TracksHistoryRepository
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.settings.data.impl.SettingsProviderImpl
import com.example.playlistmaker.settings.domain.api.SettingsProvider
import com.example.playlistmaker.sharing.data.impl.SharingProviderImpl
import com.example.playlistmaker.sharing.domain.api.SharingProvider
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    factory<SharingProvider> {
        SharingProviderImpl(androidContext())
    }

    factory<SettingsProvider> {
        SettingsProviderImpl(get())
    }

    factory<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    factory<TracksHistoryRepository> {
        LocalTracksHistoryRepositoryImpl(get())
    }

    single {
        SearchHistory(get(), get())
    }

    single<ApiClient> {
        RetrofitApiClient(get())
    }

    single<SearchApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchApi::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
    }

    factory { Gson() }
}