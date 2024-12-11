package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.api.SharingProvider

class SharingInteractorImpl(private val provider: SharingProvider): SharingInteractor {
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