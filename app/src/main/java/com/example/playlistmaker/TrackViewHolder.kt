package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackViewHolder(val item: View, private val onClick: ((Track) -> Unit?)?) : RecyclerView.ViewHolder(item) {
    private val cover: ImageView = item.findViewById(R.id.foundTrackCover)
    private val name: TextView = item.findViewById(R.id.foundTrackName)
    private val artist: TextView = item.findViewById(R.id.foundTrackArtist)
    private val duration: TextView = item.findViewById(R.id.foundTrackDuration)
    private val itemContainer: View = item.findViewById(R.id.foundTrackItem)

    fun bind(model: Track) {
        Glide.with(cover.context)
            .load(model.artworkUrl)
            .placeholder(R.drawable.tracklist_album_placeholder)
            .fitCenter()
            .centerCrop()
            .into(cover)
        name.text = model.trackName
        artist.text = model.artistName
        duration.text = " • " + model.trackTime
        if (onClick != null) {
            itemContainer.setOnClickListener { onClick.invoke(model) }
        }
    }
}