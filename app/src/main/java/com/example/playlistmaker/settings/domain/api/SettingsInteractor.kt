package com.example.playlistmaker.settings.domain.api

interface SettingsInteractor {
    fun toggleDarkTheme(enable: Boolean)
    fun isDarkThemeEnabled(): Boolean
}