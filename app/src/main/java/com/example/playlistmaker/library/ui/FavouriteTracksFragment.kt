package com.example.playlistmaker.library.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavouritesBinding
import com.example.playlistmaker.player.ui.AudioPlayerFragment
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.tracks.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTracksFragment : Fragment() {

    companion object {
        fun newInstance() = FavouriteTracksFragment()
    }

    private val viewModel: FavouriteTracksViewModel by viewModel<FavouriteTracksViewModel>()
    private var _ui: FragmentFavouritesBinding? = null
    private val ui get() = _ui!!

    private var favTracks: MutableList<Track> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _ui = FragmentFavouritesBinding.inflate(inflater, container, false)

        ui.favTracksRecycler.layoutManager = LinearLayoutManager(requireContext())
        ui.favTracksRecycler.adapter = TrackAdapter(favTracks) { clickedTrack ->
            openTrackInPlayer(clickedTrack)
        }

        viewModel.favTracks.observe(viewLifecycleOwner) { state ->
            when (state) {
                FavTracksLoadingState.Loading -> showLoading()
                FavTracksLoadingState.Empty -> showEmptyMessage()
                is FavTracksLoadingState.Content -> {
                    showFavTracks(state.tracks)
                }

            }
        }

        return ui.root
    }

    private fun showEmptyMessage() {
        ui.groupEmptyMessage.isVisible = true
        ui.progressFavTracks.isVisible = false
        ui.favTracksRecycler.isVisible = false
    }

    private fun showFavTracks(tracks: List<Track>) {

        favTracks.clear()
        favTracks.addAll(tracks)
        ui.groupEmptyMessage.isVisible = false
        ui.favTracksRecycler.isVisible = true
        ui.progressFavTracks.isVisible = false
        ui.favTracksRecycler.adapter?.notifyDataSetChanged()
    }

    private fun showLoading() {
        ui.groupEmptyMessage.isVisible = false
        ui.favTracksRecycler.isVisible = false
        ui.progressFavTracks.isVisible = true
    }

    private fun openTrackInPlayer(track: Track) {
        findNavController().navigate(R.id.action_mediaLibraryFragment_to_audioPlayerFragment2, AudioPlayerFragment.createArgs(track))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateFavTracks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _ui = null
    }
}