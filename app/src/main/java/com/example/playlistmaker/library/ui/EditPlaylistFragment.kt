package com.example.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.models.Playlist
import java.io.File

private const val ARG_PLAYLIST_ID = "PLAYLIST_ID"
private const val ARG_PLAYLIST_NAME = "PLAYLIST_NAME"
private const val ARG_PLAYLIST_DESCRIPTION = "PLAYLIST_DESCRIPTION"
private const val ARG_PLAYLIST_COVER = "PLAYLIST_COVER"

class EditPlaylistFragment(): NewPlaylistCreationFragment() {

    private var _playlist: Playlist? = null
    private val playlist get() = _playlist!!

    override val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            exitConfirmDialog(getString(R.string.editPlaylistExitDialogTitle))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _playlist = parseArgs(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        if (playlist != null) {
            ui.newPlaylistName.setText(playlist.playlistName)
            ui.newPlaylistDescription.setText(playlist.description)
            if (playlist.coverPath.isNotEmpty()) {
                val file = File(playlist.coverPath)
                if (file.exists()) {
                    ui.newPlaylistImage.setImageURI(file.toUri())
                }
            }
        }

        ui.btnNewPlaylistCreate.text = getString(R.string.editPlaylistBtnSave)
        ui.btnNewPlaylistCreate.setOnClickListener {
            val cover = newPlaylistCover
            if (playlist != null) {
                playlist.playlistName = newPlaylistName
                playlist.description = newPlaylistDescription
                if (cover != null) {
                    playlist.coverPath = savePlaylistCoverImage(cover, newPlaylistName)
                }
                viewmodel.updatePlaylist(playlist)
            }

            findNavController().popBackStack()
        }

        ui.newPlaylistHeader.text = getString(R.string.editPlaylistHeader)
        ui.newPlaylistHeader.setOnClickListener {
            exitConfirmDialog(getString(R.string.editPlaylistExitDialogTitle))
        }

        return view
    }


    companion object {
        fun createArgs(playlist: Playlist) = bundleOf(
            Pair(ARG_PLAYLIST_ID, playlist.id),
            Pair(ARG_PLAYLIST_NAME, playlist.playlistName),
            Pair(ARG_PLAYLIST_DESCRIPTION, playlist.description),
            Pair(ARG_PLAYLIST_COVER, playlist.coverPath)
        )

        private fun parseArgs(args: Bundle?): Playlist? {
            val id: Int = args?.getInt(ARG_PLAYLIST_ID) ?: -1
            val name = args?.getString(ARG_PLAYLIST_NAME) ?: ""
            val desc = args?.getString(ARG_PLAYLIST_DESCRIPTION) ?: ""
            val cover = args?.getString(ARG_PLAYLIST_COVER) ?: ""
            if (id > 0) {
                return Playlist(
                    id = id,
                    playlistName = name,
                    description = desc,
                    coverPath = cover)
            } else {
                return null
            }
        }
    }
}