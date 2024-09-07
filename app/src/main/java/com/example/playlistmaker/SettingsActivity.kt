package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<View>(R.id.btnBackFromSettings)
        val darkThemeButton = findViewById<View>(R.id.btnDarkTheme)
        val shareButton = findViewById<View>(R.id.btnShare)
        val supportButton = findViewById<View>(R.id.btnSupport)
        val userAgreeButton = findViewById<View>(R.id.btnUserAgreement)

        backButton.setOnClickListener {
            val goBackIntent = Intent(this, MainActivity::class.java)
            startActivity(goBackIntent)
        }

        darkThemeButton.setOnClickListener {
            Toast.makeText(this@SettingsActivity, "darkThemeButton Click", Toast.LENGTH_SHORT).show()
        }

        shareButton.setOnClickListener {
            Toast.makeText(this@SettingsActivity, "shareButton Click", Toast.LENGTH_SHORT).show()
        }

        supportButton.setOnClickListener {
            Toast.makeText(this@SettingsActivity, "supportButton Click", Toast.LENGTH_SHORT).show()
        }

        userAgreeButton.setOnClickListener {
            Toast.makeText(this@SettingsActivity, "userAgreeButton Click", Toast.LENGTH_SHORT).show()
        }
    }
}