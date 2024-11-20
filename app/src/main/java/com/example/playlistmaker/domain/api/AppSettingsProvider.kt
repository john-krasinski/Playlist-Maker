package com.example.playlistmaker.domain.api

interface AppSettingsProvider {
    fun toggleDarkTheme(enable: Boolean)
    fun isDarkThemeEnabled(): Boolean
}