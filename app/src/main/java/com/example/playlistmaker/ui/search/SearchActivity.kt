package com.example.playlistmaker.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator.getTrackClearHistoryUseCase
import com.example.playlistmaker.Creator.getTrackGetUseCase
import com.example.playlistmaker.Creator.getTrackSaveUseCase
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.SearchTrackPresenter
import handler
import textOfSearch
import SEARCH_DEBOUNCE_DELAY as SEARCH_DEBOUNCE_DELAY1


class SearchActivity : AppCompatActivity() {

    private lateinit var textViewSearchHistory: TextView
    private lateinit var buttonClearSearchHistory: Button
    private lateinit var searchField: EditText
    private lateinit var textSearch: String
    private lateinit var searchProgressBar: ProgressBar
    lateinit var recyclerViewSongs: RecyclerView
    lateinit var adapterSearch: AdapterSearch
    lateinit var adapterSearchHistory: AdapterSearchHistory
    private lateinit var downloadFailButton: Button
    private val searchRequest = Runnable { search() }
    private val searchTrackPresenter by lazy { SearchTrackPresenter(this) }
    private val trackClearHistoryUseCase by lazy { getTrackClearHistoryUseCase(this.applicationContext) }
    private val trackGetUseCase by lazy { getTrackGetUseCase(this.applicationContext) }
    private val trackSaveUseCase by lazy { getTrackSaveUseCase(this.applicationContext) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        textSearch = ""

        recyclerViewSongs = findViewById(R.id.recyclerView_songs)
        recyclerViewSongs.layoutManager = LinearLayoutManager(this)
        adapterSearch = AdapterSearch(trackSaveUseCase)
        adapterSearchHistory = AdapterSearchHistory()
        recyclerViewSongs.adapter = adapterSearch

        downloadFailButton = findViewById(R.id.button_download_fail)

        val buttonBack = findViewById<Button>(R.id.back_button_search_act)

        textViewSearchHistory = findViewById(R.id.text_view_search_history)

        buttonClearSearchHistory = findViewById(R.id.button_clear_search_history)

        val clearButton = findViewById<Button>(R.id.clear_text_search)

        searchField = findViewById(R.id.search_bar)

        searchProgressBar = findViewById(R.id.search_progressBar)

        buttonBack.setOnClickListener {
            finish()
        }

        if (savedInstanceState != null) {
            searchField.setText(textSearch)
        }

        buttonClearSearchHistory.setOnClickListener {
            trackClearHistoryUseCase.execute()
            adapterSearchHistory.tracks.clear()
            searchHistoryVisib(SearchHistoryVisibility.GONE)
            recyclerViewSongs.adapter?.notifyDataSetChanged()
        }

        downloadFailButton.setOnClickListener { search() }

        clearButton.setOnClickListener {
            searchField.setText("")
            adapterSearch.tracks.clear()
            errorVisibility(
                SearchActItemsVisib.SUCCESS
            )
        }

        val searchActivityTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                if (searchField.hasFocus() && searchField.text.isEmpty() && checkHistory()
                ) {
                    searchHistoryVisib(SearchHistoryVisibility.VISIBLE)
                    errorVisibility(SearchActItemsVisib.SUCCESS)
                    recyclerViewSongs.adapter = adapterSearchHistory
                    refreshHistory()
                } else {
                    searchHistoryVisib(SearchHistoryVisibility.GONE)
                    adapterSearchHistory.tracks.clear()
                    adapterSearch.tracks.clear()
                    recyclerViewSongs.adapter?.notifyDataSetChanged()
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                textSearch = searchField.text.toString()
            }

            fun clearButtonVisibility(s: CharSequence?): Int {
                return if (s.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        }

        searchField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchField.text.isEmpty() && checkHistory()) {
                searchHistoryVisib(SearchHistoryVisibility.VISIBLE)
                recyclerViewSongs.adapter = adapterSearchHistory
                refreshHistory()
            } else searchHistoryVisib(SearchHistoryVisibility.GONE)
        }
        searchField.addTextChangedListener(searchActivityTextWatcher)
    }

    private fun search() {
        searchProgressBar.visibility = View.VISIBLE
        searchTrackPresenter.searchTrack(searchField.text.toString())
    }

    fun searchDebounce() {
        handler.removeCallbacks(searchRequest)
        handler.postDelayed(searchRequest, SEARCH_DEBOUNCE_DELAY1)
    }

    fun searchHistoryVisib(visibility: SearchHistoryVisibility) {
        when (visibility) {
            SearchHistoryVisibility.VISIBLE -> textViewSearchHistory.visibility = View.VISIBLE
            SearchHistoryVisibility.GONE -> textViewSearchHistory.visibility = View.GONE
        }
        buttonClearSearchHistory.visibility = textViewSearchHistory.visibility
    }

    fun checkHistory(): Boolean = trackGetUseCase.execute().isNotEmpty()
    fun refreshHistory() {
        adapterSearchHistory.tracks = trackGetUseCase.execute().toMutableList()
        recyclerViewSongs.adapter?.notifyDataSetChanged()
    }

    fun errorVisibility(result: SearchActItemsVisib) {
        val linearLayoutDownloadFail: LinearLayout = findViewById(R.id.linearlayout_download_fail)
        val downloadFailTextView: TextView = findViewById(R.id.textview_download_fail)
        val downloadFailImageView: ImageView = findViewById(R.id.imageview_download_fail)

        when (result) {
            SearchActItemsVisib.SUCCESS -> {
                recyclerViewSongs.visibility = View.VISIBLE
                linearLayoutDownloadFail.visibility = View.GONE
                downloadFailButton.visibility = View.GONE
            }
            SearchActItemsVisib.EMPTY_SEARCH -> {
                recyclerViewSongs.visibility = View.GONE
                linearLayoutDownloadFail.visibility = View.VISIBLE
                downloadFailButton.visibility = View.GONE
                downloadFailImageView.setImageResource(R.drawable.serch_zero)
                downloadFailTextView.setText(R.string.search_fail)
            }
            SearchActItemsVisib.CONNECTION_ERROR -> {
                recyclerViewSongs.visibility = View.GONE
                linearLayoutDownloadFail.visibility = View.VISIBLE
                downloadFailButton.visibility = View.VISIBLE
                downloadFailImageView.setImageResource(R.drawable.no_internet_connection)
                downloadFailTextView.setText(R.string.internet_lost_connection)
            }
        }
        searchProgressBar.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(textOfSearch, textSearch)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textSearch = savedInstanceState.getString(textOfSearch, "")
    }
}

enum class SearchActItemsVisib {
    CONNECTION_ERROR,
    EMPTY_SEARCH,
    SUCCESS
}

enum class SearchHistoryVisibility {
    VISIBLE,
    GONE
}