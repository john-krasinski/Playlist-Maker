package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.Locale

class SearchActivity : AppCompatActivity() {

    var query: String = ""
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val searchApi = retrofit.create<SearchApi>()
    private var history: SearchHistory? = null
    private var historyVisilble = true
    private val onTrackClick = { track:Track ->
        history?.add(track)
        if (historyVisilble) {
            reDrawHistory()
        }
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

        val backButton = findViewById<View>(R.id.btnBackFromSearch)
        val inputEditText = findViewById<EditText>(R.id.searchBox)
        val clearButton = findViewById<ImageView>(R.id.searchBoxClearIcon)
        val clearHistoryButton = findViewById<Button>(R.id.btnClearSearchHistory)

        history = SearchHistory((applicationContext as App).preferences())

        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        clearHistoryButton.setOnClickListener {
            history!!.clear()
            setHistoryVisibility(false)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.searchRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TrackAdapter(listOf())

        prepareSearchBox(inputEditText, clearButton, recyclerView, savedInstanceState)
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

    private fun prepareSearchBox(inputEditText: EditText, clearButton: View, foundTracksView: RecyclerView, savedInstanceState: Bundle?) {

        inputEditText.isFocusedByDefault = true

        setHistoryVisibility(true)

        clearButton.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            foundTracksView.adapter = TrackAdapter(listOf())
            inputEditText.setText("")
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
                    }
                } else {
                    query = s.toString()
                    //doSearch(query)
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


    private fun doSearch(text: String) {
        searchApi.doSearch(text).enqueue(object : Callback<SearchResponse> {

            override fun onResponse(call: Call<SearchResponse>,response: Response<SearchResponse>) {
                setHistoryVisibility(false)

                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null && resp.resultCount > 0 && resp.results.size > 0 ) {
                        showFoundTracks(resp.results)
                    } else {
                        showNotFound()
                    }
                } else {
                    showNotFound()
                    val errorBody = response.errorBody()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                setHistoryVisibility(false)
                showNetworkError()
            }
        })
    }

    private fun showFoundTracks(trackInfo: List<ResponseTrackInfo>) {
        val foundTracksView = findViewById<RecyclerView>(R.id.searchRecycler)
        val foundTracks = trackInfo.map {
            Track(it.trackName,
                it.artistName,
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis),
                it.artworkUrl100,
                it.trackId)
        }
        foundTracksView.adapter = TrackAdapter(foundTracks, onTrackClick)
    }

    private fun showNotFound() {
        val foundTracksView = findViewById<RecyclerView>(R.id.searchRecycler)
        foundTracksView.adapter = SearchErrorAdapter(
            SearchError(getString(R.string.errorTracksNotFound),getDrawable(R.drawable.track_not_found),null, null)
        )
    }

    private fun showNetworkError() {
        val foundTracksView = findViewById<RecyclerView>(R.id.searchRecycler)
        val onClick = { doSearch(query) }
        foundTracksView.adapter = SearchErrorAdapter(
            SearchError(getString(R.string.errorNetworkError), getDrawable(R.drawable.network_error),getString(R.string.errorPageBtnTextUpdate), onClick)
        )
        val updateButton = findViewById<Button>(R.id.errorPageBtn)
    }

    private fun setHistoryVisibility(visible:Boolean) {

//        val foundTracksView = findViewById<RecyclerView>(R.id.searchRecycler)
        val historyHeader = findViewById<TextView>(R.id.searchHistoryHeader)
        val clearHistoryButton = findViewById<Button>(R.id.btnClearSearchHistory)

        if (visible && (history!!.curSize > 0)) {
            historyHeader.visibility = View.VISIBLE
            clearHistoryButton.visibility = View.VISIBLE
            reDrawHistory()
        } else {
            historyHeader.visibility = View.GONE
            clearHistoryButton.visibility = View.GONE
//            foundTracksView.adapter = TrackAdapter(listOf())
        }
        historyVisilble = visible

    }

    private fun reDrawHistory() {
        val foundTracksView = findViewById<RecyclerView>(R.id.searchRecycler)
        foundTracksView.adapter = TrackAdapter(history!!.get(), onTrackClick)
    }

}