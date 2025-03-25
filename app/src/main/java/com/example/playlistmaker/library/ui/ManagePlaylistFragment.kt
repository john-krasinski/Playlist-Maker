package com.example.playlistmaker.library.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentManagePlaylistBinding
import com.example.playlistmaker.library.domain.models.EMPTY_PLAYLIST_INFO
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.player.ui.AudioPlayerFragment
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.tracks.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

private const val ARG_PLAYLIST_ID = "PLAYLIST_ID"

class ManagePlaylistFragment : Fragment() {

    private var _ui: FragmentManagePlaylistBinding? = null
    private val ui get() = _ui!!

    private val viewmodel: ManagePlaylistViewModel by viewModel<ManagePlaylistViewModel>()
    private var playlistId: Int = -1
    private var playlist = EMPTY_PLAYLIST_INFO

    private val onTrackClick:(Track) -> Unit = { track ->
        openTrackInPlayer(track)
    }
    private val onLongTrackClick: (Track) -> Unit = { track ->
        handleLongTrackClick(track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playlistId = arguments?.getInt(ARG_PLAYLIST_ID) ?: -1
        viewmodel.getPlaylistFullInfo(playlistId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _ui = FragmentManagePlaylistBinding.inflate(layoutInflater, container, false)

        renderShareButton()
        renderActionsButton()

        ui.createdPlaylistsRecycler.layoutManager = LinearLayoutManager(requireContext())
        ui.createdPlaylistsRecycler.adapter = TrackAdapter(playlist.tracks, onTrackClick, onLongTrackClick)

        ui.btnBackFromPlaylist.setOnClickListener {
            findNavController().popBackStack()
        }

        viewmodel.playlist.observe(viewLifecycleOwner) {
            if (it == null) {
                findNavController().popBackStack()
            } else {
                playlist = it
                renderPlaylist()
            }
        }

        return ui.root
    }

    private fun renderShareButton() {
        ui.btnPlaylistShare.setOnClickListener {
            handleShareClick()
        }
    }

    private fun renderActionsButton() {

        val bottomSheet = BottomSheetBehavior.from(ui.actionsBottomSheet)
        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        ui.bottomSheetBackLayer.isVisible = false
                        viewmodel.getPlaylistFullInfo(playlistId)
                    }
                    else -> {
                        ui.bottomSheetBackLayer.isVisible = true
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        ui.btnPlaylistSettings.setOnClickListener {
            bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        //  bottomsheet buttons

        ui.actionsSharePlaylist.setOnClickListener {
            handleShareClick()
            bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
        }

        ui.actionsDeletePlaylist.setOnClickListener {
            createDialogWindow(
                title = getString(R.string.removePlaylistDialogTitle),
                message = getString(R.string.removePlaylistDialogMessage)
            )
                .setPositiveButton(
                    getString(R.string.removePlaylistDialogBtnYes)
                ) { p0, p1 ->
                    viewmodel.deletePlaylist(Playlist.fromFullInfo(playlist))
//                    findNavController().popBackStack()
                }
                .setNegativeButton(
                    getString(R.string.removePlaylistDialogBtnNo),
                    null
                )
                .show()
        }

        ui.actionsEditPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.editPlaylistFragment,
                EditPlaylistFragment.createArgs(Playlist.fromFullInfo(playlist))
            )
        }
    }

    private fun renderPlaylist() {

        //  cover
        val cover = File(playlist.coverPath)
        if (cover.exists()) {
            val uri = cover.toUri()
            ui.actionsPlaylistInfo.playlistCover.setImageURI(uri)
            ui.playlistCoverImage.setImageURI(uri)
        } else {
            val placeholder = requireContext().getDrawable(R.drawable.player_album_placeholder)
            ui.actionsPlaylistInfo.playlistCover.setImageDrawable(placeholder)
            ui.playlistCoverImage.setImageDrawable(placeholder)
        }

        //  name
        ui.playlistName.text = playlist.playlistName
        ui.actionsPlaylistInfo.playlistName.text = playlist.playlistName

        //  description
        ui.desctiption.text = playlist.description

        //  tracks
        val tracksBottomSheet = BottomSheetBehavior.from(ui.tracksBottomSheet)
        if (playlist.tracks.isEmpty()) {
            ui.emptyPlaylistMessageGroup.isVisible = true
            tracksBottomSheet.isHideable = true
            tracksBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            ui.emptyPlaylistMessageGroup.isVisible = false
            tracksBottomSheet.isHideable = false
            val adapter = ui.createdPlaylistsRecycler.adapter as? TrackAdapter
            adapter?.updateTracks(playlist.tracks)
            adapter?.notifyDataSetChanged()
            tracksBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        //  num tracks
        val numTracksString = requireContext().resources
            .getQuantityString(R.plurals.numPlaylistSongs, playlist.tracks.size, playlist.tracks.size)
        ui.numSongs.text = numTracksString
        ui.actionsPlaylistInfo.numTracks.text = numTracksString

        //  duration
        var allTracksDuration = 0
        playlist.tracks.forEach {
            allTracksDuration += it.trackTimeMillis
        }
        ui.duration.text = requireContext().resources
            .getQuantityString(
                R.plurals.playlistDurationMinutes,
                allTracksDuration,
                SimpleDateFormat("m", Locale.getDefault()).format(allTracksDuration)
            )
    }


    private fun createDialogWindow(title: String, message: String): MaterialAlertDialogBuilder {
        ui.bottomSheetBackLayer.isVisible = true
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setOnDismissListener { ui.bottomSheetBackLayer.isVisible = false }
    }

    private fun handleShareClick() {

        if (playlist.tracks.isEmpty()) {
            createDialogWindow("",getString(R.string.noTrackInPlaylistForSharing))
                .show()
        } else {

            val numTracksString = requireContext()
                .resources
                .getQuantityString(R.plurals.numPlaylistSongs, playlist.tracks.size, playlist.tracks.size)
            var message = "${playlist.playlistName}\n${playlist.description}\n${numTracksString}"
            playlist.tracks.forEachIndexed { index, track ->
                val trackString = "\n$index. ${track.artistName} - ${track.trackName} (${track.trackTime})"
                message += trackString
            }

            viewmodel.sharePlaylist(message)
        }
    }

    private fun handleLongTrackClick(track: Track) {
        createDialogWindow(
            title = getString(R.string.removeTrackDialogTitle),
            message = getString(R.string.removeTrackDialogMessage)
        ).setPositiveButton(
            getString(R.string.removeTrackDialogBtnYes)
        ) { p0, p1 ->
            viewmodel.removeTrackFromPlaylist(track, playlistId)
            viewmodel.getPlaylistFullInfo(playlistId)
        }
        .setNegativeButton(
            getString(R.string.removeTrackDialogBtnNo),
            null
        )
        .show()
    }

    private fun openTrackInPlayer(track: Track) {
        findNavController().navigate(R.id.action_managePlaylistFragment_to_audioPlayerFragment2, AudioPlayerFragment.createArgs(track))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _ui = null
    }

    override fun onResume() {
        super.onResume()
        if (playlistId > 0) {
            viewmodel.getPlaylistFullInfo(playlistId)
        }
    }

    companion object {
        fun prepareArgs(playlist: Playlist): Bundle {
            return bundleOf(
                Pair(ARG_PLAYLIST_ID, playlist.id)
            )
        }
    }
}