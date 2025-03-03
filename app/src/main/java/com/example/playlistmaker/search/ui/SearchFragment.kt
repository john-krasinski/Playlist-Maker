package com.example.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.AudioPlayerFragment
import com.example.playlistmaker.search.data.SearchError
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.error.SearchErrorAdapter
import com.example.playlistmaker.search.ui.tracks.TrackAdapter
import com.example.playlistmaker.search.ui.tracks.TrackSearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


const val SEARCH_DEBOUNCE_DELAY_MS: Long = 2000

class SearchFragment : Fragment() {

    private var _ui: FragmentSearchBinding? = null
    private val ui get() = _ui!!

    private val searchTracksViewModel: SearchTracksViewModel by viewModel<SearchTracksViewModel>()

    private var historyTracks: MutableList<Track> = mutableListOf()
    var query: String = ""
    private var historyVisible = true

    var jobDebounceSearch: Job? = null

    private val onTrackClick = { track: Track ->
        searchTracksViewModel.addToHistory(track)
        openTrackInPlayer(track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            query = savedInstanceState.getString(SEARCH_QUERY, QUERY_DEF)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _ui = FragmentSearchBinding.inflate(layoutInflater)

        searchTracksViewModel.state.observe(viewLifecycleOwner) {
            renderSearchState(it)
        }

        ui.searchRecycler.layoutManager = LinearLayoutManager(requireContext())
        ui.searchRecycler.adapter = TrackAdapter(historyTracks, onTrackClick)

        ui.btnClearSearchHistory.setOnClickListener {
            searchTracksViewModel.clearHistory()
            setHistoryVisibility(false)
        }

        prepareSearchBox(savedInstanceState)

        val root = ui.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _ui = null
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, query)
    }

    private fun renderSearchState(state: TrackSearchState) {
        when (state) {
            is TrackSearchState.Initial -> {
                historyTracks.clear()
                historyTracks.addAll(state.historyTracks)
                setHistoryVisibility(historyTracks.isNotEmpty())
                if (historyVisible) {
                    reDrawHistory()
                }
            }
            is TrackSearchState.Content -> {
                showFoundTracks(state.foundTracks)
            }
            TrackSearchState.NotFound -> {
                showNotFound()
            }
            is TrackSearchState.Error -> {
                showNetworkError()
            }
            TrackSearchState.Loading -> {
                showProgress()
            }
        }
    }

    private fun prepareSearchBox(savedInstanceState: Bundle?) {

        ui.searchBox.isFocusedByDefault = true

        setHistoryVisibility(historyTracks.isNotEmpty())

        ui.searchBoxClearIcon.setOnClickListener {
            resetSearch()
        }

        if (savedInstanceState != null) {
            ui.searchBox.setText(query)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    if (ui.searchBox.hasFocus()) {
                        setHistoryVisibility(historyTracks.isNotEmpty())
                        jobDebounceSearch?.cancel()
                    }
                } else {
                    val newQuery = s.toString()
                    //  disable unneeded search triggered by
                    //  onTextChanged() after returning from player screen
                    if (!query.equals(newQuery)) {
                        query = s.toString()
                        debounceSearch()
                    }
                }
                ui.searchBoxClearIcon.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        ui.searchBox.addTextChangedListener(simpleTextWatcher)
        ui.searchBox.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                jobDebounceSearch?.cancel()
                doSearch(query)
                true
            }
            false
        }
        ui.searchBox.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && ui.searchBox.text.isEmpty()) {
                setHistoryVisibility(historyTracks.isNotEmpty())
            }
        }
    }

    private fun debounceSearch() {
        jobDebounceSearch?.cancel()
        jobDebounceSearch = lifecycleScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_MS)
            doSearch(query)
        }
    }

    private fun resetSearch() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(ui.searchBox.windowToken, 0)
        ui.searchRecycler.adapter = TrackAdapter(listOf())
        ui.searchBox.setText("")
        ui.searchBox.isFocusedByDefault = true
        searchTracksViewModel.resetSearch()
    }

    private fun showProgress() {
        setHistoryVisibility(false)
        ui.progressSearch.isVisible = true
    }

    private fun hideProgress() {
        ui.progressSearch.isVisible = false
    }

    private fun doSearch(text: String) {
        showProgress()
        searchTracksViewModel.search(text)
    }

    private fun showFoundTracks(tracks: List<Track>) {
        hideProgress()
        ui.searchRecycler.adapter = TrackAdapter(tracks, onTrackClick)
        ui.searchRecycler.isVisible = true
    }

    private fun showNotFound() {
        hideProgress()
        ui.searchRecycler.adapter = SearchErrorAdapter(
            SearchError(getString(R.string.errorTracksNotFound),requireContext().getDrawable(R.drawable.track_not_found),null, null)
        )
        ui.searchRecycler.isVisible = true
    }

    private fun showNetworkError() {
        hideProgress()
        val onClick = { debounceSearch() }
        ui.searchRecycler.adapter = SearchErrorAdapter(
            SearchError(getString(R.string.errorNetworkError), requireContext().getDrawable(R.drawable.network_error),getString(
                R.string.errorPageBtnTextUpdate
            ), onClick)
        )
        ui.searchRecycler.isVisible = true
    }


    private fun setHistoryVisibility(visible:Boolean) {
        hideProgress()
        if (visible) {
            ui.searchHistoryGroup.isVisible = true
            reDrawHistory()
            ui.searchRecycler.isVisible = true
        } else {
            ui.searchHistoryGroup.isVisible = false
            ui.searchRecycler.isVisible = false
        }
        historyVisible = visible
    }

    private fun reDrawHistory() {
        ui.searchRecycler.adapter?.notifyDataSetChanged()
    }

    private fun openTrackInPlayer(track: Track) {
        track.isFavourite = searchTracksViewModel.isTrackFavourite(track)
        findNavController().navigate(R.id.action_searchFragment_to_audioPlayerFragment22, AudioPlayerFragment.createArgs(track))
    }

    override fun onResume() {
        super.onResume()

        searchTracksViewModel.refreshViewModel()
    }

    companion object {
        const val SEARCH_QUERY = "PRODUCT_AMOUNT"
        const val QUERY_DEF = ""
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment()
    }
}