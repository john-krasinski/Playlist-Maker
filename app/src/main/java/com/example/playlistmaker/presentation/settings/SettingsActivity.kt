package com.example.playlistmaker.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.App
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

        backButton.setOnClickListener {
            finish()
        }

        darkThemeButton.isChecked = App.useDarkTheme
        darkThemeButton.setOnCheckedChangeListener { btn, isChecked ->
            (applicationContext as App).toggleDarkTheme(isChecked)
        }

        shareButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.ShareAppMessage))
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        supportButton.setOnClickListener {
            val sendIntent: Intent =  Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, getString(R.string.SupportEmail))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.SupportMessageText))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.SupportMessageHeader))
            }
            startActivity(sendIntent)
        }

        userAgreeButton.setOnClickListener {
            val sendIntent: Intent =  Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.LicenseUrl))
            }
            startActivity(sendIntent)
        }
    }
}