package com.example.playlistmaker.main.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.ui.SettingsActivity
import com.example.playlistmaker.library.ui.MediaLibraryActivity
import com.example.playlistmaker.search.ui.SearchActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if ((application as App).settings().isDarkThemeEnabled()) {
            enableEdgeToEdge()
        } else {
            enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(getColor(R.color.white), Color.TRANSPARENT))
        }

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSearch = findViewById<Button>(R.id.btnSearch)
        btnSearch.setOnClickListener {
            val goSearchIntent = Intent(this, SearchActivity::class.java)
            startActivity(goSearchIntent)
        }

        val btnMediaLibrary = findViewById<Button>(R.id.btnMediaLibrary)
        btnMediaLibrary.setOnClickListener {
            val goLibraryIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(goLibraryIntent)
        }

        val btnSettings = findViewById<Button>(R.id.btnSettings)
        btnSettings.setOnClickListener {
            val goSettingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(goSettingsIntent)
        }
    }
}