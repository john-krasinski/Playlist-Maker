package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val USE_DARK_THEME_KEY = "use_dark_theme"

const val TRACK_NAME_KEY = "track_name"
const val TRACK_ID_KEY = "track_id"
const val ARTIST_NAME_KEY = "artist_name"
const val ALBUM_NAME_KEY = "album_name"
const val RELEASE_YEAR_KEY = "release_year"
const val GENRE_KEY = "genre"
const val COUNTRY_KEY = "country"
const val TRACK_DURATION_KEY = "duration"
const val ARTWORK_URL_KEY = "artwork_url"

class App : Application() {

    companion object {
        public var useDarkTheme = false
    }

    override fun onCreate() {
        super.onCreate()
        useDarkTheme = preferences().getBoolean(USE_DARK_THEME_KEY,false)
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

        preferences()
            .edit()
            .putBoolean(USE_DARK_THEME_KEY, useDarkTheme)
            .apply()

    }

    public fun preferences(): SharedPreferences {
        return getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
    }
}