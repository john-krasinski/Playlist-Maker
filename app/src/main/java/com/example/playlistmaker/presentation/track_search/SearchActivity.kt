package com.example.playlistmaker.presentation.track_search

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
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.ALBUM_NAME_KEY
import com.example.playlistmaker.ARTIST_NAME_KEY
import com.example.playlistmaker.ARTWORK_URL_KEY
import com.example.playlistmaker.presentation.player.AudioPlayerActivity
import com.example.playlistmaker.COUNTRY_KEY
import com.example.playlistmaker.Creator
import com.example.playlistmaker.GENRE_KEY
import com.example.playlistmaker.PREVIEW_URL_KEY
import com.example.playlistmaker.R
import com.example.playlistmaker.RELEASE_YEAR_KEY
import com.example.playlistmaker.presentation.search_error.SearchErrorAdapter
import com.example.playlistmaker.TRACK_DURATION_KEY
import com.example.playlistmaker.TRACK_ID_KEY
import com.example.playlistmaker.TRACK_NAME_KEY
import com.example.playlistmaker.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.SearchError
import com.example.playlistmaker.domain.models.Track

const val SEARCH_DEBOUNCE_DELAY_MS: Long = 2000

class SearchActivity : AppCompatActivity() {

    var query: String = ""

    private lateinit var historyInteractor: TracksHistoryInteractor
    private var historyVisible = true

    val handler = Handler(Looper.getMainLooper())
    val searchRunnable = Runnable { doSearch(query) }

    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: View
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var clearHistoryButton: Button
    private lateinit var progress: ProgressBar
    private lateinit var historyGroup: Group

    private val onTrackClick = { track: Track ->
        historyInteractor.addTrack(track)
        if (historyVisible) {
            reDrawHistory(historyInteractor.getTracks())
        }
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

        backButton = findViewById<View>(R.id.btnBackFromSearch)
        inputEditText = findViewById<EditText>(R.id.searchBox)
        clearButton = findViewById<ImageView>(R.id.searchBoxClearIcon)
        clearHistoryButton = findViewById<Button>(R.id.btnClearSearchHistory)
        recyclerView = findViewById<RecyclerView>(R.id.searchRecycler)
        progress = findViewById<ProgressBar>(R.id.progressSearch)
        historyGroup = findViewById<Group>(R.id.searchHistoryGroup)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TrackAdapter(listOf())

        historyInteractor = Creator.provideHistoryInteractor(this)
        if (historyInteractor.getTracks().isEmpty()) {
            setHistoryVisibility(false)
        }

        backButton.setOnClickListener {
            finish()
        }

        clearHistoryButton.setOnClickListener {
            historyInteractor.clear()
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

    private fun prepareSearchBox(savedInstanceState: Bundle?) {

        inputEditText.isFocusedByDefault = true

        setHistoryVisibility(true)

        clearButton.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            recyclerView.adapter = TrackAdapter(listOf())
            inputEditText.setText("")
            inputEditText.isFocusedByDefault = true
        }

        if (savedInstanceState != null) {
            inputEditText.setText(query)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    if (inputEditText.hasFocus()) {
                        setHistoryVisibility(true)
                        handler.removeCallbacks(searchRunnable)
                    }
                } else {
                    query = s.toString()
                    debounceSearch()
                }
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handler.removeCallbacks(searchRunnable)
                doSearch(query)
                true
            }
            false
        }
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) {
                setHistoryVisibility(true)
            }
        }
    }

    private fun debounceSearch() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MS)
    }

    private fun showProgress() {
        setHistoryVisibility(false)
        progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progress.visibility = View.GONE
    }

    private fun doSearch(text: String) {

        showProgress()
        Creator.provideTracksInteractor().searchTracks(text, object : TracksInteractor.TracksConsumer {

            override fun onSuccess(foundTracks: List<Track>) {
                hideProgress()
                if (foundTracks.isEmpty()) {
                    showNotFound()
                } else {
                    showFoundTracks(foundTracks)
                }
            }

            override fun onError(message: String) {
                hideProgress()
                showNetworkError()
            }
        })
    }

    private fun showFoundTracks(tracks: List<Track>) {
        recyclerView.adapter = TrackAdapter(tracks, onTrackClick)
        recyclerView.visibility = View.VISIBLE
    }

    private fun showNotFound() {
        recyclerView.adapter = SearchErrorAdapter(
            SearchError(getString(R.string.errorTracksNotFound),getDrawable(R.drawable.track_not_found),null, null)
        )
        recyclerView.visibility = View.VISIBLE
    }

    private fun showNetworkError() {
        val onClick = { debounceSearch() }
        recyclerView.adapter = SearchErrorAdapter(
            SearchError(getString(R.string.errorNetworkError), getDrawable(R.drawable.network_error),getString(
                R.string.errorPageBtnTextUpdate
            ), onClick)
        )
        recyclerView.visibility = View.VISIBLE
        val updateButton = findViewById<Button>(R.id.errorPageBtn)
    }


    private fun setHistoryVisibility(visible:Boolean) {
        if (visible) {
            val tracks = historyInteractor.getTracks()
            if (tracks.isNotEmpty()) {
                historyGroup.visibility = View.VISIBLE
                reDrawHistory(tracks)
                recyclerView.visibility = View.VISIBLE
            }
        } else {
            historyGroup.visibility = View.GONE
            recyclerView.visibility = View.GONE
        }
        historyVisible = visible
    }

    private fun reDrawHistory(tracks: List<Track>) {
        recyclerView.adapter = TrackAdapter(tracks, onTrackClick)
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