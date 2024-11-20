package com.example.playlistmaker.data

import android.app.Application.MODE_PRIVATE
import android.content.Context
import com.example.playlistmaker.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.domain.api.AppSettingsProvider



class AppSettingsProviderImpl(private val context: Context): AppSettingsProvider {

    companion object {
        const val USE_DARK_THEME_KEY = "use_dark_theme"
    }

    private val preferences = context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

    override fun toggleDarkTheme(enable: Boolean) {
        preferences.edit()
            .putBoolean(USE_DARK_THEME_KEY, enable)
            .apply()
    }

    override fun isDarkThemeEnabled(): Boolean {
        return preferences.getBoolean(USE_DARK_THEME_KEY,false)
    }
}