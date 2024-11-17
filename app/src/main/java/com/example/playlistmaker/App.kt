package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.AppSettingsInteractor


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

class App : Application() {

    companion object {
        public var useDarkTheme = false
    }

    override fun onCreate() {
        super.onCreate()
        useDarkTheme = settings().isDarkThemeEnabled()
        toggleDarkTheme(useDarkTheme)
    }

    public fun toggleDarkTheme(paramUseDarkTheme: Boolean) {
        useDarkTheme = paramUseDarkTheme

        AppCompatDelegate.setDefaultNightMode(
            if (paramUseDarkTheme)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )

        settings().toggleDarkTheme(useDarkTheme)
    }

    public fun settings(): AppSettingsInteractor {
        return Creator.provideAppSettingsInteractor(this)
    }
}