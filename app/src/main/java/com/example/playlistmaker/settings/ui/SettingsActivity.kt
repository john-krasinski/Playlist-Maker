package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.factory(application, this)
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