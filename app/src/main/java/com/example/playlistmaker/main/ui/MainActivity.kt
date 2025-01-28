package com.example.playlistmaker.main.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.example.playlistmaker.R
import com.example.playlistmaker.library.ui.MediaLibraryFragment
import com.example.playlistmaker.search.ui.SearchFragment
import com.example.playlistmaker.settings.ui.SettingsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        enableEdgeToEdge()
//        if ((application as App).settings.isDarkThemeEnabled()) {
//            enableEdgeToEdge()
//        } else {
//            enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
//        }


        if (savedInstanceState == null) {

            supportFragmentManager.commit {
//                replace(R.id.main, MediaLibraryFragment())
                replace(R.id.main, SearchFragment())
//                replace(R.id.main, SettingsFragment())
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}