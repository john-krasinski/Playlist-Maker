package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.AppRemoteActionsInteractor
import com.example.playlistmaker.domain.api.AppRemoteActionsProvider

class AppRemoteActionsInteractorImpl(private val provider: AppRemoteActionsProvider): AppRemoteActionsInteractor {
    override fun shareApp() {
        provider.shareApp()
    }

    override fun contactSupport() {
        provider.contactSupport()
    }

    override fun readUserAgreement() {
        provider.readUserAgreement()
    }
}