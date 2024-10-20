package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class AudioPlayer : AppCompatActivity() {

    var isPlaying = false
    var isLiked = false
    var isAddedToPlaylist = false

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
        val btnPlayPause = findViewById<ImageView>(R.id.btnPlayPause)
        val btnLike = findViewById<ImageView>(R.id.btnLike)
        val btnPlaylist = findViewById<ImageView>(R.id.btnAddToPlaylist)
        val timePlayed = findViewById<TextView>(R.id.timePlayed)
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

        btnPlayPause.setImageDrawable(getDrawable(R.drawable.play))
        btnPlayPause.setOnClickListener {
            btnPlayPause.setImageDrawable(
                if (isPlaying) {
                    getDrawable(R.drawable.play)
                } else {
                    getDrawable(R.drawable.pause)
                }
            )
            isPlaying = !isPlaying
        }

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

        return Track(trackId,trackName,artistName,albumName,trackTime,artworkUrl.replaceAfterLast('/',"512x512bb.jpg"),country,genre,year)
    }
}
