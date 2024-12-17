package com.example.playlistmaker.settings.ui


import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val application: App,
    private val sharingInteractor: SharingInteractor
): AndroidViewModel(application) {

    private val darkThemeEnabled = MutableLiveData<Boolean>(application.settings.isDarkThemeEnabled())

    fun isDarkThemeEnabled() : LiveData<Boolean> = darkThemeEnabled

    fun toggleDarkTheme(enable: Boolean) {
        application.toggleDarkTheme(enable)
        darkThemeEnabled.postValue(enable)
    }

    fun shareApp() = sharingInteractor.shareApp()
    fun contactSupport() = sharingInteractor.contactSupport()
    fun readUserAgreement() = sharingInteractor.readUserAgreement()

    companion object {
        fun factory(application: App,
                    sharingInteractor: SharingInteractor
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SettingsViewModel(application, sharingInteractor)
                }
            }
        }
    }
}