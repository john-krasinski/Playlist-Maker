package com.example.playlistmaker.library.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.player.ui.LibraryPlaylistsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    private val viewmodel: PlaylistsViewModel by viewModel<PlaylistsViewModel>()

    private var _ui: FragmentPlaylistsBinding? = null
    private val ui get() = _ui!!

    private var playlists = mutableListOf<Playlist>()
    private var onPlaylistClick: (Playlist) -> Unit = { playlist ->
        Toast.makeText(
            requireContext(),
            "Open playlist ${playlist.playlistName}",
            Toast.LENGTH_SHORT)
            .show()
        viewmodel.deletePlaylist(playlist)
        playlists.remove(playlist)
        drawPlaylists()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _ui = FragmentPlaylistsBinding.inflate(inflater,container,false)
        ui.btnNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.newPlaylistCreationFragment)
        }

        viewmodel.playlists.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                drawEmpty()
            } else {
                playlists.clear()
                playlists.addAll(it)
                drawPlaylists()
            }
        }

        ui.libraryPlaylistsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        ui.libraryPlaylistsRecycler.adapter = LibraryPlaylistsAdapter(playlists, onPlaylistClick)
        return ui.root
    }

    private fun drawEmpty() {
        ui.libraryPlaylistsRecycler.isVisible = false
        ui.emptyPlaylistsGroup.isVisible =  true
    }

    private fun drawPlaylists() {
        ui.emptyPlaylistsGroup.isVisible =  false
        ui.libraryPlaylistsRecycler.isVisible = true
        ui.libraryPlaylistsRecycler.adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _ui = null
    }

    override fun onResume() {
        super.onResume()
        viewmodel.updatePlaylists()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}