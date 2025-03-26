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
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistCreationBinding
import com.example.playlistmaker.library.domain.models.Playlist
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream


open class NewPlaylistCreationFragment : Fragment() {

    protected val viewmodel: PlaylistsViewModel by viewModel<PlaylistsViewModel>()

    protected var _ui: FragmentNewPlaylistCreationBinding? = null
    protected val ui get() = _ui!!

    protected var newPlaylistCover: Uri? = null
    protected var newPlaylistName: String = ""
    protected var newPlaylistDescription: String = ""

    protected open val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            exitConfirmDialog(getString(R.string.playlistCreationExitDialogTitle))
        }
    }

    protected val playlistCoverChooser =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                ui.newPlaylistImage.setImageURI(uri)
                newPlaylistCover = uri
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
            val cover = newPlaylistCover
            if (cover != null) {
                val savedCover = savePlaylistCoverImage(cover, newPlaylistName)
                viewmodel.createPlaylist(newPlaylistName, newPlaylistDescription, savedCover)
            } else {
                viewmodel.createPlaylist(newPlaylistName, newPlaylistDescription, "")
            }

            findNavController().popBackStack()
        }

        ui.newPlaylistHeader.setOnClickListener {
            exitConfirmDialog(getString(R.string.playlistCreationExitDialogTitle))
        }

        ui.newPlaylistName.doOnTextChanged { text, start, before, count ->
            newPlaylistName = text?.toString() ?: ""
            if (text?.isNotEmpty() ?: false) {
                ui.btnNewPlaylistCreate.isEnabled = true
            } else {
                ui.btnNewPlaylistCreate.isEnabled = false
            }
        }
        ui.newPlaylistDescription.doOnTextChanged { text, start, before, count ->
            newPlaylistDescription = text?.toString() ?: ""
        }

        return ui.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, backPressedCallback)
    }

    protected fun exitConfirmDialog(title: String) {
        if (newPlaylistCover != null || newPlaylistName.isNotEmpty() || newPlaylistDescription.isNotEmpty()) {
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


    protected fun savePlaylistCoverImage(uri: Uri, name: String): String {

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