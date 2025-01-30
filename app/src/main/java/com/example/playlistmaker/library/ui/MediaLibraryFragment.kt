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

    private var _ui: FragmentMediaLibraryBinding? = null
    private val ui get() = _ui!!

    private var _tabMediator: TabLayoutMediator? = null
    private val tabMediator get() = _tabMediator!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _ui = FragmentMediaLibraryBinding.inflate(layoutInflater)

        ui.libraryPager.adapter = LibraryPagerAdapter(childFragmentManager, lifecycle)
        _tabMediator = TabLayoutMediator(ui.libraryTabs, ui.libraryPager) { tab, pos ->
            tab.text = LibraryPagerAdapter.tabs[pos]
        }

        tabMediator.attach()

        val root = ui.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
        _tabMediator = null
        _ui = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}