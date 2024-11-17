package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.AppSettingsInteractor
import com.example.playlistmaker.domain.api.AppSettingsProvider

class AppSettingsInteractorImpl(private val provider: AppSettingsProvider): AppSettingsInteractor {
    override fun toggleDarkTheme(enable: Boolean) {
        provider.toggleDarkTheme(enable)
    }

    override fun isDarkThemeEnabled(): Boolean {
        return provider.isDarkThemeEnabled()
    }
}