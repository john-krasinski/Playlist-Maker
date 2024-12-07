package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.api.SettingsProvider

class SettingsInteractorImpl(private val provider: SettingsProvider): SettingsInteractor {
    override fun toggleDarkTheme(enable: Boolean) {
        provider.toggleDarkTheme(enable)
    }

    override fun isDarkThemeEnabled(): Boolean {
        return provider.isDarkThemeEnabled()
    }
}