package com.example.playlistmaker.settings.data.impl


import android.content.SharedPreferences
import com.example.playlistmaker.settings.domain.api.SettingsProvider



class SettingsProviderImpl(private val preferences: SharedPreferences): SettingsProvider {

    companion object {
        const val USE_DARK_THEME_KEY = "use_dark_theme"
    }

    override fun toggleDarkTheme(enable: Boolean) {
        preferences.edit()
            .putBoolean(USE_DARK_THEME_KEY, enable)
            .apply()
    }

    override fun isDarkThemeEnabled(): Boolean {
        return preferences.getBoolean(USE_DARK_THEME_KEY,false)
    }
}