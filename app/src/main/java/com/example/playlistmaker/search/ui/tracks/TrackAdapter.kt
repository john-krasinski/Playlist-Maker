package com.example.playlistmaker.search.ui.tracks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.search.domain.models.Track

class TrackAdapter(
    private val tracks: List<Track>,
    private val onClick: ((Track) -> Unit?)? = null
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TrackItemBinding.inflate(layoutInflater, parent, false)
        return TrackViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    class TrackViewHolder(val binding: TrackItemBinding, private val onClick: ((Track) -> Unit?)?) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Track) {
            Glide.with(binding.foundTrackCover.context)
                .load(model.artworkUrl)
                .placeholder(R.drawable.tracklist_album_placeholder)
                .fitCenter()
                .centerCrop()
                .into(binding.foundTrackCover)
            binding.foundTrackName.text = model.trackName
            binding.foundTrackArtist.text = model.artistName
            binding.foundTrackDuration.text = " • " + model.trackTime
            if (onClick != null) {
                binding.foundTrackItem.setOnClickListener { onClick.invoke(model) }
            }
        }
    }
}