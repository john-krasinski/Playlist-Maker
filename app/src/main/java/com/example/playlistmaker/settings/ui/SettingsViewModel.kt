package com.example.playlistmaker.settings.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.creator.Creator

class SettingsViewModel(private val application: Application, srcActivityContext: Context): AndroidViewModel(application) {

    private val sharingInteractor = Creator.provideAppRemoteActionsInteractor(srcActivityContext)

    private val darkThemeEnabled = MutableLiveData<Boolean>((application as App).settings().isDarkThemeEnabled())

    fun isDarkThemeEnabled() : LiveData<Boolean> = darkThemeEnabled

    fun toggleDarkTheme(enable: Boolean) {
        (application as App).toggleDarkTheme(enable)
        darkThemeEnabled.postValue(enable)
    }

    fun shareApp() = sharingInteractor.shareApp()
    fun contactSupport() = sharingInteractor.contactSupport()
    fun readUserAgreement() = sharingInteractor.readUserAgreement()

    companion object {
        fun factory(application: Application, srcActivityContext: Context): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SettingsViewModel(application, srcActivityContext)
                }
            }
        }
    }
}