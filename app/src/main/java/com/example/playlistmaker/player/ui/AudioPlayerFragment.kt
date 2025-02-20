package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.ALBUM_NAME_KEY
import com.example.playlistmaker.ARTIST_NAME_KEY
import com.example.playlistmaker.ARTWORK_URL_KEY
import com.example.playlistmaker.COUNTRY_KEY
import com.example.playlistmaker.GENRE_KEY
import com.example.playlistmaker.PREVIEW_URL_KEY
import com.example.playlistmaker.R
import com.example.playlistmaker.RELEASE_YEAR_KEY
import com.example.playlistmaker.TRACK_DURATION_KEY
import com.example.playlistmaker.TRACK_ID_KEY
import com.example.playlistmaker.TRACK_NAME_KEY
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.search.domain.models.Track
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
                PREVIEW_URL_KEY to track.previewUrl
            )
    }

    private lateinit var currentTrack: Track
    private var isLiked = false
    private var isAddedToPlaylist = false
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

        player.curState().observe(viewLifecycleOwner) { state ->
            when (state) {
                PlayerState.Prepared -> {
                    ui.btnPlayPause.isEnabled = true
                    ui.btnPlayPause.setImageDrawable(requireContext().getDrawable(R.drawable.play))
                    ui.timePlayed.text = "00:00"
                    jobUpdateTime?.cancel()
                }
                PlayerState.Playing -> {
                    ui.btnPlayPause.setImageDrawable(requireContext().getDrawable(R.drawable.pause))
                    jobUpdateTime = lifecycleScope.launch {
                        while (true) {
                            delay(POSITION_UPDATE_INTERVAL_MS)
                            ui.timePlayed.text =  SimpleDateFormat("mm:ss", Locale.getDefault())
                                .format(player.position())
                        }
                    }
                }
                PlayerState.Paused -> {
                    ui.btnPlayPause.setImageDrawable(requireContext().getDrawable(R.drawable.play))
                    jobUpdateTime?.cancel()
                }
                else -> {}
            }
        }

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

        ui.btnPlayPause.isEnabled = false
        ui.btnPlayPause.setImageDrawable(requireContext().getDrawable(R.drawable.play))
        ui.btnPlayPause.setOnClickListener {
            player.playbackControl()
        }

        ui.btnLike.setOnClickListener {
            ui.btnLike.setImageDrawable(
                if (isLiked) {
                    requireContext().getDrawable(R.drawable.like_empty)
                } else {
                    requireContext().getDrawable(R.drawable.like)
                }
            )
            isLiked = !isLiked
        }

        ui.btnAddToPlaylist.setOnClickListener {
            ui.btnAddToPlaylist.setImageDrawable(
                if (isAddedToPlaylist) {
                    requireContext().getDrawable(R.drawable.add)
                } else {
                    requireContext().getDrawable(R.drawable.in_playlist)
                }
            )
            isAddedToPlaylist = !isAddedToPlaylist
        }

        ui.btnBackFromPlayer.setOnClickListener {
            findNavController().navigateUp()
        }

        val root = ui.root
        return root
    }


    override fun onPause() {
        super.onPause()
        player.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
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

        currentTrack = Track(trackId,trackName,artistName,albumName,trackTime,artworkUrl.replaceAfterLast('/',"512x512bb.jpg"),country,genre,year,previewUrl)
    }
}