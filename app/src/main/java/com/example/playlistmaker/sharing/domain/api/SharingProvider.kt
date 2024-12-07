package com.example.playlistmaker.sharing.domain.api

interface SharingProvider {
    fun shareApp()
    fun contactSupport()
    fun readUserAgreement()
}