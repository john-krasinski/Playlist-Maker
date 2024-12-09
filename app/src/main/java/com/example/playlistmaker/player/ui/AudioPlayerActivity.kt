package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
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
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

const val POSITION_UPDATE_INTERVAL_MS: Long = 300

class AudioPlayerActivity : AppCompatActivity() {

    private var isLiked = false
    private var isAddedToPlaylist = false

    private lateinit var player: PlayerViewModel
    private lateinit var ui: ActivityAudioPlayerBinding

    val handler = Handler(Looper.getMainLooper())
    private val updateTimeRunnable = object: Runnable {
        override fun run() {
            ui.timePlayed.text =  SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(player.position())
            handler.postDelayed(this, POSITION_UPDATE_INTERVAL_MS)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ui = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(ui.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentTrack = getCurrentTrack()

        player = ViewModelProvider(
            this,
            PlayerViewModel.factory(currentTrack)
        )[PlayerViewModel::class.java]



        player.curState().observe(this) { state ->
            when (state) {
                PlayerState.Prepared -> {
                    ui.btnPlayPause.isEnabled = true
                    ui.btnPlayPause.setImageDrawable(getDrawable(R.drawable.play))
                    ui.timePlayed.text = "00:00"
                    handler.removeCallbacks(updateTimeRunnable)
                }
                PlayerState.Playing -> {
                    ui.btnPlayPause.setImageDrawable(getDrawable(R.drawable.pause))
                    handler.post(updateTimeRunnable)
                }
                PlayerState.Paused -> {
                    ui.btnPlayPause.setImageDrawable(getDrawable(R.drawable.play))
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

        ui.btnBackFromPlayer.setOnClickListener {
            finish()
        }

        ui.btnPlayPause.isEnabled = false
        ui.btnPlayPause.setImageDrawable(getDrawable(R.drawable.play))
        ui.btnPlayPause.setOnClickListener {
            player.playbackControl()
        }

        ui.btnLike.setOnClickListener {
            ui.btnLike.setImageDrawable(
                if (isLiked) {
                    getDrawable(R.drawable.like_empty)
                } else {
                    getDrawable(R.drawable.like)
                }
            )
            isLiked = !isLiked
        }

        ui.btnAddToPlaylist.setOnClickListener {
            ui.btnAddToPlaylist.setImageDrawable(
                if (isAddedToPlaylist) {
                    getDrawable(R.drawable.add)
                } else {
                    getDrawable(R.drawable.in_playlist)
                }
            )
            isAddedToPlaylist = !isAddedToPlaylist
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

    private fun getCurrentTrack(): Track {
        val trackId = intent.getIntExtra(TRACK_ID_KEY, -1)
        val trackName = intent.getStringExtra(TRACK_NAME_KEY) ?: getString(R.string.trackDetailUnknown)
        val artistName = intent.getStringExtra(ARTIST_NAME_KEY) ?: getString(R.string.trackDetailUnknown)
        val albumName = intent.getStringExtra(ALBUM_NAME_KEY) ?: getString(R.string.trackDetailUnknown)
        val year = intent.getStringExtra(RELEASE_YEAR_KEY) ?: getString(R.string.trackDetailUnknown)
        val genre = intent.getStringExtra(GENRE_KEY) ?: getString(R.string.trackDetailUnknown)
        val country = intent.getStringExtra(COUNTRY_KEY) ?: getString(R.string.trackDetailUnknown)
        val trackTime = intent.getStringExtra(TRACK_DURATION_KEY) ?: getString(R.string.trackDetailUnknown)
        val artworkUrl = intent.getStringExtra(ARTWORK_URL_KEY) ?: ""
        val previewUrl = intent.getStringExtra(PREVIEW_URL_KEY) ?: ""

        return Track(trackId,trackName,artistName,albumName,trackTime,artworkUrl.replaceAfterLast('/',"512x512bb.jpg"),country,genre,year,previewUrl)
    }

}
