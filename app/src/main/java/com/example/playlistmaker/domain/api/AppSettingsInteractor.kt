package com.example.playlistmaker.domain.api

interface AppSettingsInteractor {
    fun toggleDarkTheme(enable: Boolean)
    fun isDarkThemeEnabled(): Boolean
}