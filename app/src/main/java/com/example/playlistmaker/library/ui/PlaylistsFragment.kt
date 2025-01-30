package com.example.playlistmaker.library.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding

class PlaylistsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    private val playlists: PlaylistsViewModel by viewModels()

    private var _ui: FragmentPlaylistsBinding? = null
    private val ui get() = _ui!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _ui = FragmentPlaylistsBinding.inflate(inflater,container,false)

        return ui.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _ui = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}