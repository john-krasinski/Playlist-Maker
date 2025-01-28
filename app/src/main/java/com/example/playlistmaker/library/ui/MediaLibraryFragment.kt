package com.example.playlistmaker.library.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryFragment : Fragment() {

    companion object {
        fun newInstance() = MediaLibraryFragment()
    }

    private lateinit var ui: FragmentMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ui = FragmentMediaLibraryBinding.inflate(layoutInflater)

        ui.libraryPager.adapter = LibraryPagerAdapter(childFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(ui.libraryTabs, ui.libraryPager) { tab, pos ->
            tab.text = LibraryPagerAdapter.tabs[pos]
        }
        tabMediator.attach()

        return ui.root
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}