package com.example.playlistmaker

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.content.Context

class TrackViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val cover: ImageView = item.findViewById(R.id.foundTrackCover)
    private val name: TextView = item.findViewById(R.id.foundTrackName)
    private val details: TextView = item.findViewById(R.id.foundTrackDetails)

    fun bind(model: Track) {
        Glide.with(cover.context)
            .load(model.artworkUrl)
            .placeholder(R.drawable.album_placeholder)
            .fitCenter()
            .centerCrop()
            .into(cover)
        name.text = model.trackName
        details.text = model.artistName + " • " + model.trackTime
    }
}