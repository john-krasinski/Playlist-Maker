package com.example.playlistmaker.library.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var ui: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(ui.root)

        ui.libraryPager.adapter = LibraryPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(ui.libraryTabs, ui.libraryPager) { tab, pos ->
            tab.text = LibraryPagerAdapter.tabs[pos]
        }
        tabMediator.attach()

        ui.btnBackFromLibrary.setOnClickListener { finish() }

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.media_library)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        tabMediator.detach()
    }
}