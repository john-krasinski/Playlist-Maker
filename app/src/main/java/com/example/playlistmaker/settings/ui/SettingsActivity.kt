package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<View>(R.id.btnBackFromSettings)
        val darkThemeButton = findViewById<SwitchMaterial>(R.id.btnToggleDarkTheme)
        val shareButton = findViewById<View>(R.id.btnShare)
        val supportButton = findViewById<View>(R.id.btnSupport)
        val userAgreeButton = findViewById<View>(R.id.btnUserAgreement)

        val remoteActions = Creator.provideAppRemoteActionsInteractor()

        backButton.setOnClickListener {
            finish()
        }

        darkThemeButton.isChecked = App.useDarkTheme
        darkThemeButton.setOnCheckedChangeListener { btn, isChecked ->
            (applicationContext as App).toggleDarkTheme(isChecked)
        }

        shareButton.setOnClickListener {
            remoteActions.shareApp()
        }

        supportButton.setOnClickListener {
            remoteActions.contactSupport()
        }

        userAgreeButton.setOnClickListener {
            remoteActions.readUserAgreement()
        }
    }
}