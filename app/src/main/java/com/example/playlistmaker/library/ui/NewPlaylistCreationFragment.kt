package com.example.playlistmaker.library.ui

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistCreationBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream


class NewPlaylistCreationFragment : Fragment() {

    private val viewmodel: PlaylistsViewModel by viewModel<PlaylistsViewModel>()

    private var _ui: FragmentNewPlaylistCreationBinding? = null
    private val ui get() = _ui!!

    private var playlistCover: Uri? = null
    private var playlistName: String = ""
    private var playlistDescription: String = ""

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            exitConfirmDialog()
        }
    }

    private val playlistCoverChooser =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                ui.newPlaylistImage.setImageURI(uri)
                playlistCover = uri
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _ui = FragmentNewPlaylistCreationBinding.inflate(layoutInflater)

        ui.newPlaylistImage.setOnClickListener {
            playlistCoverChooser.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        ui.btnNewPlaylistCreate.isEnabled = false
        ui.btnNewPlaylistCreate.setOnClickListener {
            val cover = playlistCover
            if (cover != null) {
                val savedCover = savePlaylistCoverImage(cover, playlistName)
                viewmodel.createPlaylist(playlistName, playlistDescription, savedCover)
            } else {
                viewmodel.createPlaylist(playlistName, playlistDescription, "")
            }

            findNavController().popBackStack()
        }

        ui.newPlaylistHeader.setOnClickListener {
            exitConfirmDialog()
        }

        ui.newPlaylistName.doOnTextChanged { text, start, before, count ->
            playlistName = text?.toString() ?: ""
            if (text?.isNotEmpty() ?: false) {
                ui.btnNewPlaylistCreate.isEnabled = true
            } else {
                ui.btnNewPlaylistCreate.isEnabled = false
            }
        }
        ui.newPlaylistDescription.doOnTextChanged { text, start, before, count ->
            playlistDescription = text?.toString() ?: ""
        }

        return ui.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, backPressedCallback)
    }

    private fun exitConfirmDialog() {
        if (playlistCover != null || playlistName.isNotEmpty() || playlistDescription.isNotEmpty()) {
            ui.dialogBackLayer.isVisible = true
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.playlistCreationExitDialogTitle))
                .setMessage(getString(R.string.playlistCreationExitDialogMessage))
                .setOnDismissListener { ui.dialogBackLayer.isVisible = false }
                .setNegativeButton(getString(R.string.playlistCreationExitDialogCancelButton), null)
                .setPositiveButton(
                    getString(R.string.playlistCreationExitDialogExitButton),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            findNavController().popBackStack()
                        }
                    })
                .show()
        } else {
            findNavController().popBackStack()
        }
    }


    private fun savePlaylistCoverImage(uri: Uri, name: String): String {

        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_covers")
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val file = File(filePath, "$name.jpg")
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        return file.absolutePath
    }
}