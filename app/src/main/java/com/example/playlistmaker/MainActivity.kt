package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSearch = findViewById<Button>(R.id.btnSearch)
        btnSearch.setOnClickListener {
            val goSearchIntent = Intent(this, SearchActivity::class.java)
            goSearchIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(goSearchIntent)
        }

        val btnMediaLibrary = findViewById<Button>(R.id.btnMediaLibrary)
        btnMediaLibrary.setOnClickListener {
            val goLibraryIntent = Intent(this, MediaLibraryActivity::class.java)
            goLibraryIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(goLibraryIntent)
        }

        val btnSettings = findViewById<Button>(R.id.btnSettings)
        btnSettings.setOnClickListener {
            val goSettingsIntent = Intent(this, SettingsActivity::class.java)
            goSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(goSettingsIntent)
        }
    }
}