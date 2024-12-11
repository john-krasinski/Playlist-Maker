package com.example.playlistmaker.settings.domain.api

interface SettingsProvider {
    fun toggleDarkTheme(enable: Boolean)
    fun isDarkThemeEnabled(): Boolean
}