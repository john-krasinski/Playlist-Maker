package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.ALBUM_NAME_KEY
import com.example.playlistmaker.ARTIST_NAME_KEY
import com.example.playlistmaker.ARTWORK_URL_KEY
import com.example.playlistmaker.COUNTRY_KEY
import com.example.playlistmaker.GENRE_KEY
import com.example.playlistmaker.IS_FAVOURITE_TRACK_KEY
import com.example.playlistmaker.PREVIEW_URL_KEY
import com.example.playlistmaker.R
import com.example.playlistmaker.RELEASE_YEAR_KEY
import com.example.playlistmaker.TRACK_DURATION_KEY
import com.example.playlistmaker.TRACK_ID_KEY
import com.example.playlistmaker.TRACK_NAME_KEY
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

const val POSITION_UPDATE_INTERVAL_MS: Long = 300

class AudioPlayerFragment : Fragment() {

    private val player: PlayerViewModel by viewModel<PlayerViewModel> { parametersOf(currentTrack) }
    private var _ui: FragmentAudioPlayerBinding? = null
    private val ui get() = _ui!!

    private var playlists = mutableListOf<Playlist>()
    private val onPlaylistClick: (Playlist) -> Unit = { playlist ->
        handleAddingToPlaylist(playlist)
    }

    companion object {
        fun createArgs(track: Track) =
            bundleOf(
                TRACK_ID_KEY to track.trackId,
                TRACK_NAME_KEY to track.trackName,
                ARTIST_NAME_KEY to track.artistName,
                ALBUM_NAME_KEY to track.albumName,
                RELEASE_YEAR_KEY to track.year,
                GENRE_KEY to track.genre,
                COUNTRY_KEY to track.country,
                TRACK_DURATION_KEY to track.trackTime,
                ARTWORK_URL_KEY to track.artworkUrl.replaceAfterLast('/',"512x512bb.jpg"),
                PREVIEW_URL_KEY to track.previewUrl,
                IS_FAVOURITE_TRACK_KEY to track.isFavourite
            )
    }

    private lateinit var currentTrack: Track

    private var hideBottomSheet = true
    var jobUpdateTime: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getCurrentTrack()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _ui = FragmentAudioPlayerBinding.inflate(inflater)

        player.isFavourite.observe(viewLifecycleOwner) { isLiked ->
            drawLikeButton(isLiked)
        }

        player.playlists.observe(viewLifecycleOwner) {
            playlists.clear()
            playlists.addAll(it)
            ui.createdPlaylistsRecycler.adapter?.notifyDataSetChanged()
        }

        player.curState().observe(viewLifecycleOwner) { state ->
            when (state) {
                PlayerState.Prepared -> { drawPlayerPrepared() }
                PlayerState.Playing -> { drawPlayerPlaying() }
                PlayerState.Paused -> { drawPlayerPaused() }
                else -> {}
            }
        }

        drawLikeButton(currentTrack.isFavourite)
        ui.btnLike.setOnClickListener {
            player.processLikeClick()
        }

        drawTrackInfo()

        ui.btnPlayPause.isEnabled = false
        ui.btnPlayPause.setImageDrawable(requireContext().getDrawable(R.drawable.play))
        ui.btnPlayPause.setOnClickListener {
            player.playbackControl()
        }

        drawPlaylistButton()

        ui.btnBackFromPlayer.setOnClickListener {
            findNavController().navigateUp()
        }

        return ui.root
    }

    private fun drawPlayerPrepared() {
        ui.btnPlayPause.isEnabled = true
        ui.btnPlayPause.setImageDrawable(requireContext().getDrawable(R.drawable.play))
        ui.timePlayed.text = "00:00"
        jobUpdateTime?.cancel()
    }

    private fun drawPlayerPlaying() {
        ui.btnPlayPause.setImageDrawable(requireContext().getDrawable(R.drawable.pause))
        jobUpdateTime = lifecycleScope.launch {
            while (true) {
                delay(POSITION_UPDATE_INTERVAL_MS)
                ui.timePlayed.text =  SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(player.position())
            }
        }
    }

    private fun drawPlayerPaused() {
        ui.btnPlayPause.setImageDrawable(requireContext().getDrawable(R.drawable.play))
        jobUpdateTime?.cancel()
    }

    private fun drawPlaylistButton() {

        ui.btnAddToPlaylist.setImageDrawable(requireContext().getDrawable(R.drawable.add))

        val bottomSheet = BottomSheetBehavior.from(ui.bottomsheetAddToPlaylist)
        drawBottomSheet(bottomSheet)

        ui.btnAddToPlaylist.setOnClickListener {
            bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun drawBottomSheet(bottomSheet: BottomSheetBehavior<ConstraintLayout>) {
        if (hideBottomSheet) {
            bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
        }

        ui.createdPlaylistsRecycler.layoutManager = LinearLayoutManager(requireContext())
        ui.createdPlaylistsRecycler.adapter = BottomSheetPlaylistsAdapter(playlists, onPlaylistClick)

        ui.newPlaylistBtn.setOnClickListener {
            hideBottomSheet = false
            findNavController().navigate(R.id.newPlaylistCreationFragment)
        }

        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        ui.bottomSheetBackLayer.isVisible = false
                        player.updatePlaylists()
                    }
                    else -> {
                        ui.bottomSheetBackLayer.isVisible = true
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun drawTrackInfo() {
        ui.trackName.text = currentTrack.trackName
        ui.artistName.text = currentTrack.artistName
        ui.detailsDurationValue.text = currentTrack.trackTime
        ui.detailsAlbumValue.text = currentTrack.albumName
        ui.detailsYearValue.text = currentTrack.year
        ui.detailsGenreValue.text = currentTrack.genre
        ui.detailsCountryValue.text = currentTrack.country

        if (currentTrack.artworkUrl.isNotEmpty()) {
            Glide.with(ui.albumImage.context)
                .load(currentTrack.artworkUrl)
                .placeholder(R.drawable.player_album_placeholder)
                .fitCenter()
                .centerCrop()
                .into(ui.albumImage)
        }
    }

    private fun drawLikeButton(isLiked: Boolean) {
        ui.btnLike.setImageDrawable(
            if (isLiked) {
                requireContext().getDrawable(R.drawable.like)
            } else {
                requireContext().getDrawable(R.drawable.like_empty)
            }
        )
    }

    private fun handleAddingToPlaylist(playlist: Playlist) {
        if (playlist.trackIDs.contains(player.track.trackId)) {
            Toast
                .makeText(
                    requireContext(),
                    requireContext().getString(R.string.alreadyInPlaylist, playlist.playlistName),
                    Toast.LENGTH_SHORT)
                .show()
        } else {
            player.addToPlaylist(playlist)
            ui.createdPlaylistsRecycler.adapter?.notifyDataSetChanged()
            Toast
                .makeText(
                    requireContext(),
                    requireContext().getString(R.string.successAddingToPlaylist, playlist.playlistName),
                    Toast.LENGTH_SHORT
                )
                .show()
            BottomSheetBehavior.from(ui.bottomsheetAddToPlaylist).state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onResume() {
        super.onResume()
        player.updatePlaylists()
    }

    override fun onPause() {
        super.onPause()
        player.pausePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _ui = null
    }

    private fun getCurrentTrack() {
        val unknown = requireContext().getString(R.string.trackDetailUnknown)

        val trackId = arguments?.getInt(TRACK_ID_KEY, -1) ?: -1
        val trackName = arguments?.getString(TRACK_NAME_KEY) ?: unknown
        val artistName = arguments?.getString(ARTIST_NAME_KEY) ?: unknown
        val albumName = arguments?.getString(ALBUM_NAME_KEY) ?: unknown
        val year = arguments?.getString(RELEASE_YEAR_KEY) ?: unknown
        val genre = arguments?.getString(GENRE_KEY) ?: unknown
        val country = arguments?.getString(COUNTRY_KEY) ?: unknown
        val trackTime = arguments?.getString(TRACK_DURATION_KEY) ?: unknown
        val artworkUrl = arguments?.getString(ARTWORK_URL_KEY) ?: ""
        val previewUrl = arguments?.getString(PREVIEW_URL_KEY) ?: ""
        val isFavourite = arguments?.getBoolean(IS_FAVOURITE_TRACK_KEY) ?: false

        currentTrack = Track(trackId,trackName,artistName,albumName,trackTime,artworkUrl.replaceAfterLast('/',"512x512bb.jpg"),country,genre,year,previewUrl,isFavourite)
    }
}