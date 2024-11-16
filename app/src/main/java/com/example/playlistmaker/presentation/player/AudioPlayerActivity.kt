package com.example.playlistmaker.presentation.player

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

enum class PlayerState {
Default, Prepared, Playing, Paused
}

const val POSITION_UPDATE_INTERVAL_MS: Long = 300

class AudioPlayerActivity : AppCompatActivity() {

    var isLiked = false
    var isAddedToPlaylist = false

    val mediaPlayer = MediaPlayer()
    var playerState = PlayerState.Default
    var currentPosition: Int = 0

    lateinit var timePlayed: TextView
    lateinit var btnPlayPause: ImageView

    val handler = Handler(Looper.getMainLooper())
    val updateTimeRunnable = object: Runnable {
        override fun run() {
            timePlayed.text =  SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            handler.postDelayed(this, POSITION_UPDATE_INTERVAL_MS)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var currentTrack = getCurrentTrack()

        val btnBack = findViewById<View>(R.id.btnBackFromPlayer)
        val artwork = findViewById<ImageView>(R.id.albumImage)
        val trackName = findViewById<TextView>(R.id.trackName)
        val artistName = findViewById<TextView>(R.id.artistName)
        btnPlayPause = findViewById<ImageView>(R.id.btnPlayPause)
        val btnLike = findViewById<ImageView>(R.id.btnLike)
        val btnPlaylist = findViewById<ImageView>(R.id.btnAddToPlaylist)
        timePlayed = findViewById<TextView>(R.id.timePlayed)
        val trackDuration = findViewById<TextView>(R.id.detailsDurationValue)
        val albumName = findViewById<TextView>(R.id.detailsAlbumValue)
        val trackYear = findViewById<TextView>(R.id.detailsYearValue)
        val trackGenre = findViewById<TextView>(R.id.detailsGenreValue)
        val trackCountry = findViewById<TextView>(R.id.detailsCountryValue)

        trackName.text = currentTrack.trackName
        artistName.text = currentTrack.artistName
        trackDuration.text = currentTrack.trackTime
        albumName.text = currentTrack.albumName
        trackYear.text = currentTrack.year
        trackGenre.text = currentTrack.genre
        trackCountry.text = currentTrack.country

        if (currentTrack.artworkUrl.isNotEmpty()) {
            Glide.with(artwork.context)
                .load(currentTrack.artworkUrl)
                .placeholder(R.drawable.player_album_placeholder)
                .fitCenter()
                .centerCrop()
                .into(artwork)
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnPlayPause.isEnabled = false
        btnPlayPause.setImageDrawable(getDrawable(R.drawable.play))
        btnPlayPause.setOnClickListener {
            playbackControl()
        }
        preparePlayer(currentTrack.previewUrl)

        btnLike.setOnClickListener {
            btnLike.setImageDrawable(
                if (isLiked) {
                    getDrawable(R.drawable.like_empty)
                } else {
                    getDrawable(R.drawable.like)
                }
            )
            isLiked = !isLiked
        }

        btnPlaylist.setOnClickListener {
            btnPlaylist.setImageDrawable(
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
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimeRunnable)
        mediaPlayer.release()
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

    private fun preparePlayer(sourceUrl: String) {
        mediaPlayer.setDataSource(sourceUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            btnPlayPause.isEnabled = true
            playerState = PlayerState.Prepared
        }
        mediaPlayer.setOnCompletionListener {
            btnPlayPause.setImageDrawable(getDrawable(R.drawable.play))
            playerState = PlayerState.Prepared
            timePlayed.text = "00:00"
            handler.removeCallbacks(updateTimeRunnable)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        btnPlayPause.setImageDrawable(getDrawable(R.drawable.pause))
        playerState = PlayerState.Playing
        handler.post(updateTimeRunnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        btnPlayPause.setImageDrawable(getDrawable(R.drawable.play))
        playerState = PlayerState.Paused
        handler.removeCallbacks(updateTimeRunnable)
    }

    private fun playbackControl() {
        when(playerState) {
            PlayerState.Playing -> {
                pausePlayer()
            }
            PlayerState.Prepared, PlayerState.Paused -> {
                startPlayer()
            }
            PlayerState.Default -> {}
        }
    }

}
