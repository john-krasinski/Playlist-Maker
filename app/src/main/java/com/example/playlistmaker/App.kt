package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val USE_DARK_THEME_KEY = "use_dark_theme"

class App : Application() {

    companion object {
        public var useDarkTheme = false
    }

    override fun onCreate() {
        super.onCreate()
        useDarkTheme = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
            .getBoolean(USE_DARK_THEME_KEY,false)

    }

    public fun toggleDarkTheme(paramUseDarkTheme: Boolean) {
        useDarkTheme = paramUseDarkTheme

        AppCompatDelegate.setDefaultNightMode(
            if (paramUseDarkTheme)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )

        getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
            .edit()
            .putBoolean(USE_DARK_THEME_KEY, useDarkTheme)
            .apply()

    }
}