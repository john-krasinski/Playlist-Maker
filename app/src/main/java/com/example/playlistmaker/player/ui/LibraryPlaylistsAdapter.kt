package com.example.playlistmaker.player.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistGridItemBinding
import com.example.playlistmaker.library.domain.models.Playlist
import java.io.File

class LibraryPlaylistsAdapter(
    private val playlists: MutableList<Playlist>,
    private val onClick: (Playlist) -> Unit
): RecyclerView.Adapter<LibraryPlaylistsAdapter.ViewHolder>() {

    class ViewHolder(
        private val context: Context,
        val binding: PlaylistGridItemBinding,
        private val onClick: (Playlist) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist) {
            val cover = File(playlist.coverPath)
            if (cover.exists()) {
                binding.playlistCover.setImageURI(cover.toUri())
            } else {
                binding.playlistCover.setImageDrawable(context.getDrawable(R.drawable.player_album_placeholder))
            }
            binding.playlistName.text = playlist.playlistName
            binding.numTracks.text = context.resources.getQuantityString(R.plurals.numPlaylistSongs, playlist.numTracks, playlist.numTracks)
            binding.playlistItem.setOnClickListener { onClick(playlist) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PlaylistGridItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(parent.context, binding, onClick)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(playlists[position])
    }
}