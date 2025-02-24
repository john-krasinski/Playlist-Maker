package com.example.playlistmaker.library.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavouriteTracksBinding

class FavouriteTracksFragment : Fragment() {

    companion object {
        fun newInstance() = FavouriteTracksFragment()
    }

    private val tracks: FavouriteTracksViewModel by viewModels()
    private var _ui: FragmentFavouriteTracksBinding? = null
    private val ui get() = _ui!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _ui = FragmentFavouriteTracksBinding.inflate(inflater, container, false)

        return ui.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _ui = null
    }
}