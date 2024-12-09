package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.ALBUM_NAME_KEY
import com.example.playlistmaker.ARTIST_NAME_KEY
import com.example.playlistmaker.ARTWORK_URL_KEY
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.COUNTRY_KEY
import com.example.playlistmaker.GENRE_KEY
import com.example.playlistmaker.PREVIEW_URL_KEY
import com.example.playlistmaker.R
import com.example.playlistmaker.RELEASE_YEAR_KEY
import com.example.playlistmaker.TRACK_DURATION_KEY
import com.example.playlistmaker.TRACK_ID_KEY
import com.example.playlistmaker.TRACK_NAME_KEY
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.data.SearchError
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.error.SearchErrorAdapter
import com.example.playlistmaker.search.ui.tracks.TrackAdapter
import com.example.playlistmaker.search.ui.tracks.TrackSearchState

const val SEARCH_DEBOUNCE_DELAY_MS: Long = 2000

class SearchActivity : AppCompatActivity() {

    private lateinit var tracksViewModel: TracksViewModel
    private var historyTracks :List<Track> = emptyList()
    private lateinit var ui: ActivitySearchBinding

    var query: String = ""

    private var historyVisible = true

    val handler = Handler(Looper.getMainLooper())
    val searchRunnable = Runnable { doSearch(query) }

    private val onTrackClick = { track: Track ->
        tracksViewModel.addToHistory(track)
        openTrackInPlayer(track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        ui = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(ui.root)

        tracksViewModel = ViewModelProvider(this, TracksViewModel.factory())[TracksViewModel::class.java]
        tracksViewModel.getState().observe(this) {
            renderSearchState(it)
        }
        tracksViewModel.getHistory().observe(this) {
            historyTracks = it
            if (historyVisible) {
                reDrawHistory()
            }
        }


        ui.searchRecycler.layoutManager = LinearLayoutManager(this)
        ui.searchRecycler.adapter = TrackAdapter(listOf())

        ui.btnBackFromSearch.setOnClickListener {
            finish()
        }

        ui.btnClearSearchHistory.setOnClickListener {
            tracksViewModel.clearHistory()
            setHistoryVisibility(false)
        }

        prepareSearchBox(savedInstanceState)
    }

    companion object {
        const val SEARCH_QUERY = "PRODUCT_AMOUNT"
        const val QUERY_DEF = ""
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, query)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        query = savedInstanceState.getString(SEARCH_QUERY, QUERY_DEF)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun renderSearchState(state: TrackSearchState) {
        when (state) {
            TrackSearchState.Initial -> {
                setHistoryVisibility(historyTracks.isNotEmpty())
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
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(ui.searchBox.windowToken, 0)
            ui.searchRecycler.adapter = TrackAdapter(listOf())
            ui.searchBox.setText("")
            ui.searchBox.isFocusedByDefault = true
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
                        handler.removeCallbacks(searchRunnable)
                    }
                } else {
                    query = s.toString()
                    debounceSearch()
                }
                ui.searchBoxClearIcon.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        ui.searchBox.addTextChangedListener(simpleTextWatcher)
        ui.searchBox.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handler.removeCallbacks(searchRunnable)
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
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MS)
    }

    private fun showProgress() {
        setHistoryVisibility(false)
        ui.progressSearch.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        ui.progressSearch.visibility = View.GONE
    }

    private fun doSearch(text: String) {
        showProgress()
        tracksViewModel.search(text)
    }

    private fun showFoundTracks(tracks: List<Track>) {
        hideProgress()
        ui.searchRecycler.adapter = TrackAdapter(tracks, onTrackClick)
        ui.searchRecycler.visibility = View.VISIBLE
    }

    private fun showNotFound() {
        hideProgress()
        ui.searchRecycler.adapter = SearchErrorAdapter(
            SearchError(getString(R.string.errorTracksNotFound),getDrawable(R.drawable.track_not_found),null, null)
        )
        ui.searchRecycler.visibility = View.VISIBLE
    }

    private fun showNetworkError() {
        hideProgress()
        val onClick = { debounceSearch() }
        ui.searchRecycler.adapter = SearchErrorAdapter(
            SearchError(getString(R.string.errorNetworkError), getDrawable(R.drawable.network_error),getString(
                R.string.errorPageBtnTextUpdate
            ), onClick)
        )
        ui.searchRecycler.visibility = View.VISIBLE
        val updateButton = findViewById<Button>(R.id.errorPageBtn)
    }


    private fun setHistoryVisibility(visible:Boolean) {
        hideProgress()
        if (visible) {
//            if (historyTracks.isNotEmpty()) {
                ui.searchHistoryGroup.visibility = View.VISIBLE
                reDrawHistory()
                ui.searchRecycler.visibility = View.VISIBLE
//            }
        } else {
            ui.searchHistoryGroup.visibility = View.GONE
            ui.searchRecycler.visibility = View.GONE
        }
        historyVisible = visible
    }

    private fun reDrawHistory() {
        ui.searchRecycler.adapter = TrackAdapter(historyTracks, onTrackClick)
    }

    private fun openTrackInPlayer(track: Track) {
        val goPlayerIntent = Intent(this, AudioPlayerActivity::class.java)
        goPlayerIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        goPlayerIntent.apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(TRACK_NAME_KEY,track.trackName)
            putExtra(TRACK_ID_KEY,track.trackId)
            putExtra(ARTIST_NAME_KEY,track.artistName)
            putExtra(ALBUM_NAME_KEY, track.albumName)
            putExtra(RELEASE_YEAR_KEY, track.year)
            putExtra(GENRE_KEY, track.genre)
            putExtra(COUNTRY_KEY, track.country)
            putExtra(TRACK_DURATION_KEY, track.trackTime)
            putExtra(ARTWORK_URL_KEY, track.artworkUrl)
            putExtra(PREVIEW_URL_KEY, track.previewUrl)
        }
        startActivity(goPlayerIntent)
    }

}