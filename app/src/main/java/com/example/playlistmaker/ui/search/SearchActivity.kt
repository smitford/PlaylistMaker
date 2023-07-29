package com.example.playlistmaker.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.models.Track
import handler
import textOfSearch
import SEARCH_DEBOUNCE_DELAY as SEARCH_DEBOUNCE_DELAY1


class SearchActivity : AppCompatActivity() {
    companion object {
        const val CLEAR_DEBOUNCE_DELAY = 500L
        const val STATE_HISTORY_SHOW = "History"
    }

    private lateinit var binding: ActivitySearchBinding

    private lateinit var textSearch: String
    lateinit var recyclerViewSongs: RecyclerView
    lateinit var adapterSearch: AdapterSearch
    lateinit var adapterSearchHistory: AdapterSearchHistory
    private val searchRequest = Runnable { search() }
    private val clear = Runnable { clearSearchField() }
    private lateinit var searchTrackViewModel: SearchTrackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val callBack = fun(track: Track) {
            searchTrackViewModel.saveTrack(track = track)
        }
        searchTrackViewModel = ViewModelProvider(
            this,
            SearchViewModelFactory(this.applicationContext)
        )[SearchTrackViewModel::class.java]

        textSearch = ""
        recyclerViewSongs = findViewById(R.id.recyclerView_songs)
        recyclerViewSongs.layoutManager = LinearLayoutManager(this)
        adapterSearch = AdapterSearch(callBack)
        adapterSearchHistory = AdapterSearchHistory()
        recyclerViewSongs.adapter = adapterSearch

        binding.backButtonSearchAct.setOnClickListener {
            finish()
        }

        if (savedInstanceState != null) {
            binding.searchBar.setText(textSearch)
        }

        binding.clearSearchHistoryLl.setOnClickListener {
            searchTrackViewModel.clearHistory()
        }

        binding.buttonDownloadFail.setOnClickListener { search() }

        binding.clearTextSearch.setOnClickListener {
            handler.removeCallbacks(searchRequest)
            clearDebounce()
        }

        searchTrackViewModel.getSearchActivityState().observe(this) { searchActivityState ->
            when (searchActivityState) {
                is SearchActivityState.Start -> {
                    elementsVisibility(SearchActItemsVis.START_VIEW)
                }

                is SearchActivityState.Loading -> {
                    elementsVisibility(SearchActItemsVis.LOADING)
                }

                is SearchActivityState.ConnectionError -> {
                    elementsVisibility(SearchActItemsVis.CONNECTION_ERROR)
                }

                is SearchActivityState.InvalidRequest -> {
                    elementsVisibility(SearchActItemsVis.EMPTY_SEARCH)
                }

                is SearchActivityState.State -> {
                    when (searchActivityState.state) {
                        STATE_HISTORY_SHOW -> {
                            elementsVisibility(SearchActItemsVis.SHOW_HISTORY)
                            recyclerViewSongs.adapter = adapterSearchHistory
                            adapterSearchHistory.tracks =
                                searchActivityState.trackList.toMutableList()
                        }

                        else -> {
                            elementsVisibility(SearchActItemsVis.SUCCESS)
                            recyclerViewSongs.adapter = adapterSearch
                            adapterSearch.tracks = searchActivityState.trackList.toMutableList()
                        }
                    }
                }
            }
        }

        val searchActivityTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearTextSearch.visibility = clearButtonVisibility(s)
                if (binding.searchBar.hasFocus() && binding.searchBar.text.isEmpty()) {
                    searchTrackViewModel.refreshHistory()
                } else {
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                textSearch = binding.searchBar.text.toString()
            }

            fun clearButtonVisibility(s: CharSequence?): Int {
                return if (s.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        }

        binding.searchBar.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchBar.text.isEmpty()) {
                searchTrackViewModel.refreshHistory()
            }
        }
        binding.searchBar.addTextChangedListener(searchActivityTextWatcher)
    }

    private fun search() =
        searchTrackViewModel.searchTrack(binding.searchBar.text.toString())

    private fun clearSearchField() {
        binding.searchBar.setText("")
        searchTrackViewModel.refreshHistory()
    }

    private fun clearDebounce() {
        handler.removeCallbacks(clear)
        handler.postDelayed(clear, CLEAR_DEBOUNCE_DELAY)
    }

    private fun elementsVisibility(result: SearchActItemsVis) {


        when (result) {
            SearchActItemsVis.SUCCESS -> {
                recyclerViewSongs.visibility = View.VISIBLE
                binding.linearlayoutDownloadFail.visibility = View.GONE
                binding.buttonDownloadFail.visibility = View.GONE
                binding.searchProgressBar.visibility = View.GONE
            }

            SearchActItemsVis.EMPTY_SEARCH -> {
                recyclerViewSongs.visibility = View.GONE
                binding.linearlayoutDownloadFail.visibility = View.VISIBLE
                binding.buttonDownloadFail.visibility = View.GONE
                binding.imageviewDownloadFail.setImageResource(R.drawable.serch_zero)
                binding.textviewDownloadFail.setText(R.string.search_fail)
                binding.searchProgressBar.visibility = View.GONE
            }

            SearchActItemsVis.CONNECTION_ERROR -> {
                recyclerViewSongs.visibility = View.GONE
                binding.linearlayoutDownloadFail.visibility = View.VISIBLE
                binding.buttonDownloadFail.visibility = View.VISIBLE
                binding.imageviewDownloadFail.setImageResource(R.drawable.no_internet_connection)
                binding.textviewDownloadFail.setText(R.string.internet_lost_connection)
                binding.searchProgressBar.visibility = View.GONE
            }

            SearchActItemsVis.SHOW_HISTORY -> {
                recyclerViewSongs.visibility = View.VISIBLE
                binding.linearlayoutDownloadFail.visibility = View.GONE
                binding.buttonDownloadFail.visibility = View.GONE
                binding.textViewSearchHistory.visibility = View.VISIBLE
                binding.searchProgressBar.visibility = View.GONE
            }

            SearchActItemsVis.START_VIEW -> {
                recyclerViewSongs.visibility = View.GONE
                binding.linearlayoutDownloadFail.visibility = View.GONE
                binding.buttonDownloadFail.visibility = View.GONE
                binding.textViewSearchHistory.visibility = View.GONE
                binding.searchProgressBar.visibility = View.GONE
            }

            SearchActItemsVis.LOADING -> {
                recyclerViewSongs.visibility = View.GONE
                binding.linearlayoutDownloadFail.visibility = View.GONE
                binding.textViewSearchHistory.visibility = View.GONE
                binding.buttonDownloadFail.visibility = View.GONE
                binding.searchProgressBar.visibility = View.VISIBLE
            }
        }
        binding.buttonClearSearchHistory.visibility = binding.textViewSearchHistory.visibility
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(textOfSearch, textSearch)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textSearch = savedInstanceState.getString(textOfSearch, "")
    }

    fun searchDebounce() {
        handler.removeCallbacks(searchRequest)
        handler.postDelayed(searchRequest, SEARCH_DEBOUNCE_DELAY1)
    }
}

enum class SearchActItemsVis {
    CONNECTION_ERROR,
    EMPTY_SEARCH,
    SUCCESS,
    SHOW_HISTORY,
    START_VIEW,
    LOADING
}
