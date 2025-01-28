package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

const val POSITION_UPDATE_INTERVAL_MS: Long = 300

class AudioPlayerFragment(private val currentTrack: Track) : Fragment() {

    private val player: PlayerViewModel by viewModel<PlayerViewModel> { parametersOf(currentTrack) }
    private lateinit var ui: FragmentAudioPlayerBinding

    companion object {
        fun newInstance(track: Track) =
            AudioPlayerFragment(track.apply {
                artworkUrl.replaceAfterLast('/',"512x512bb.jpg")
            })
    }

    private var isLiked = false
    private var isAddedToPlaylist = false
    private val handler = Handler(Looper.getMainLooper())
    private val updateTimeRunnable = object: Runnable {
        override fun run() {
            ui.timePlayed.text =  SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(player.position())
            handler.postDelayed(this, POSITION_UPDATE_INTERVAL_MS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            //  открыть экран по-умолчанию
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ui = FragmentAudioPlayerBinding.inflate(inflater)
        return ui.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        player.curState().observe(viewLifecycleOwner) { state ->
            when (state) {
                PlayerState.Prepared -> {
                    ui.btnPlayPause.isEnabled = true
                    ui.btnPlayPause.setImageDrawable(requireContext().getDrawable(R.drawable.play))
                    ui.timePlayed.text = "00:00"
                    handler.removeCallbacks(updateTimeRunnable)
                }
                PlayerState.Playing -> {
                    ui.btnPlayPause.setImageDrawable(requireContext().getDrawable(R.drawable.pause))
                    handler.post(updateTimeRunnable)
                }
                PlayerState.Paused -> {
                    ui.btnPlayPause.setImageDrawable(requireContext().getDrawable(R.drawable.play))
                    handler.removeCallbacks(updateTimeRunnable)
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

//        ui.btnBackFromPlayer.setOnClickListener {
//            finish()
//        }

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
            parentFragmentManager.popBackStack()
        }
    }


    override fun onPause() {
        super.onPause()
        player.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimeRunnable)
    }
}