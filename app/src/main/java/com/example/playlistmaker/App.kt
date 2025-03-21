package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.domainModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"

const val TRACK_NAME_KEY = "track_name"
const val TRACK_ID_KEY = "track_id"
const val ARTIST_NAME_KEY = "artist_name"
const val ALBUM_NAME_KEY = "album_name"
const val RELEASE_YEAR_KEY = "release_year"
const val GENRE_KEY = "genre"
const val COUNTRY_KEY = "country"
const val TRACK_DURATION_KEY = "duration"
const val ARTWORK_URL_KEY = "artwork_url"
const val PREVIEW_URL_KEY = "preview_url"
const val IS_FAVOURITE_TRACK_KEY = "is_fav"

class App : Application() {

    companion object {
        public var useDarkTheme = false
    }

    val settings:SettingsInteractor by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, domainModule, viewModelModule)
        }

        useDarkTheme = settings.isDarkThemeEnabled()
        toggleDarkTheme(useDarkTheme)
    }

    fun toggleDarkTheme(paramUseDarkTheme: Boolean) {
        useDarkTheme = paramUseDarkTheme

        AppCompatDelegate.setDefaultNightMode(
            if (paramUseDarkTheme)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )

        settings.toggleDarkTheme(useDarkTheme)
    }

}