package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.api.SharingProvider
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class SettingsActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModel<SettingsViewModel> {
        parametersOf((application as App))
    }

    private lateinit var ui: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(ui.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        settingsViewModel.isDarkThemeEnabled().observe(this) { isEnabled ->
            ui.btnToggleDarkTheme.isChecked = isEnabled
        }

        ui.btnBackFromSettings.setOnClickListener {
            finish()
        }

        ui.btnToggleDarkTheme.setOnCheckedChangeListener { btn, isChecked ->
            settingsViewModel.toggleDarkTheme(isChecked)
        }

        ui.btnShare.setOnClickListener {
            settingsViewModel.shareApp()
        }

        ui.btnSupport.setOnClickListener {
            settingsViewModel.contactSupport()
        }

        ui.btnUserAgreement.setOnClickListener {
            settingsViewModel.readUserAgreement()
        }
    }
}