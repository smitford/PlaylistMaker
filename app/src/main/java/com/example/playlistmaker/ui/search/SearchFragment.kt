package com.example.playlistmaker.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.utils.debounceSearch
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import textOfSearch


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var textSearch: String? = null
    private lateinit var recyclerViewSongs: RecyclerView
    private lateinit var adapterSearch: AdapterSearch
    private lateinit var adapterSearchHistory: AdapterSearchHistory
    private lateinit var onTrackClickDebounce: (Track, Boolean) -> Unit
    private var searchJob: Job? = null
    private val searchViewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounceSearch(
            delayMillis = DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track, isSearch ->
            if (isSearch) searchViewModel.saveTrack(track = track)
            val arg = SearchFragmentDirections.actionSearchFragmentToPlayerFragment(track)
            findNavController().navigate(arg)
        }


        recyclerViewSongs = binding.recyclerViewSongs
        recyclerViewSongs.layoutManager = LinearLayoutManager(requireContext())
        adapterSearch = AdapterSearch(onTrackClickDebounce, true)
        adapterSearchHistory = AdapterSearchHistory(onTrackClickDebounce, false)
        recyclerViewSongs.adapter = adapterSearch

        if (savedInstanceState != null)
            textSearch = savedInstanceState.getString(textOfSearch)

        binding.buttonClearSearchHistory.setOnClickListener {
            searchViewModel.clearHistory()
        }

        binding.buttonDownloadFail.setOnClickListener { search() }

        binding.clearTextSearch.setOnClickListener {
            searchJob?.cancel()
            searchViewModel.stopSearch()
            elementsVisibility(SearchFragmentItemsVis.START_VIEW)
            clearSearchField()
        }

        searchViewModel.getSearchActivityState()
            .observe(viewLifecycleOwner) { searchActivityState ->
                when (searchActivityState) {
                    is SearchFragmentState.Start -> {
                        elementsVisibility(SearchFragmentItemsVis.START_VIEW)
                    }

                    is SearchFragmentState.Loading -> {
                        elementsVisibility(SearchFragmentItemsVis.LOADING)
                    }

                    is SearchFragmentState.ConnectionError -> {
                        elementsVisibility(SearchFragmentItemsVis.CONNECTION_ERROR)
                    }

                    is SearchFragmentState.InvalidRequest -> {
                        elementsVisibility(SearchFragmentItemsVis.EMPTY_SEARCH)
                    }

                    is SearchFragmentState.State -> {
                        when (searchActivityState.state) {
                            STATE_HISTORY_SHOW -> {
                                elementsVisibility(SearchFragmentItemsVis.SHOW_HISTORY)
                                recyclerViewSongs.adapter = adapterSearchHistory
                                adapterSearchHistory.tracks =
                                    searchActivityState.trackList.toMutableList()
                            }

                            else -> {
                                elementsVisibility(SearchFragmentItemsVis.SUCCESS)
                                recyclerViewSongs.adapter = adapterSearch
                                adapterSearch.tracks = searchActivityState.trackList.toMutableList()
                            }
                        }
                    }
                }
            }

        val searchActivityTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                changeVisBottomNav(View.GONE)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearTextSearch.visibility = clearButtonVisibility(s)
                if (binding.searchBar.hasFocus() && binding.searchBar.text.isNullOrBlank()) {
                    searchViewModel.refreshHistory()
                } else {
                    searchJob?.cancel()
                    searchJob = viewLifecycleOwner.lifecycleScope.launch {
                        delay(SEARCH_DEBOUNCE_DELAY)
                        search()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                textSearch = binding.searchBar.text.toString()
                changeVisBottomNav(View.VISIBLE)
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
            if (hasFocus && binding.searchBar.text.isNullOrBlank()) {
                elementsVisibility(SearchFragmentItemsVis.START_VIEW)
                searchViewModel.refreshHistory()
            }
        }
        binding.searchBar.addTextChangedListener(searchActivityTextWatcher)
    }

    private fun changeVisBottomNav(focus: Int) {
        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.visibility = focus
    }

    private fun search() =
        searchViewModel.searchTrack(binding.searchBar.text.toString())

    private fun clearSearchField() {
        binding.searchBar.setText("")
        searchViewModel.refreshHistory()
    }


    private fun elementsVisibility(result: SearchFragmentItemsVis) {
        when (result) {
            SearchFragmentItemsVis.SUCCESS -> {
                recyclerViewSongs.visibility = View.VISIBLE
                binding.linearlayoutDownloadFail.visibility = View.GONE
                binding.buttonDownloadFail.visibility = View.GONE
                binding.searchProgressBar.visibility = View.GONE
            }

            SearchFragmentItemsVis.EMPTY_SEARCH -> {
                recyclerViewSongs.visibility = View.GONE
                binding.linearlayoutDownloadFail.visibility = View.VISIBLE
                binding.buttonDownloadFail.visibility = View.GONE
                binding.imageviewDownloadFail.setImageResource(R.drawable.search_zero)
                binding.textviewDownloadFail.setText(R.string.search_fail)
                binding.searchProgressBar.visibility = View.GONE
            }

            SearchFragmentItemsVis.CONNECTION_ERROR -> {
                recyclerViewSongs.visibility = View.GONE
                binding.linearlayoutDownloadFail.visibility = View.VISIBLE
                binding.buttonDownloadFail.visibility = View.VISIBLE
                binding.imageviewDownloadFail.setImageResource(R.drawable.no_internet_connection)
                binding.textviewDownloadFail.setText(R.string.internet_lost_connection)
                binding.searchProgressBar.visibility = View.GONE
            }

            SearchFragmentItemsVis.SHOW_HISTORY -> {
                recyclerViewSongs.visibility = View.VISIBLE
                binding.linearlayoutDownloadFail.visibility = View.GONE
                binding.buttonDownloadFail.visibility = View.GONE
                binding.textViewSearchHistory.visibility = View.VISIBLE
                binding.searchProgressBar.visibility = View.GONE
            }

            SearchFragmentItemsVis.START_VIEW -> {
                recyclerViewSongs.visibility = View.GONE
                binding.linearlayoutDownloadFail.visibility = View.GONE
                binding.buttonDownloadFail.visibility = View.GONE
                binding.textViewSearchHistory.visibility = View.GONE
                binding.searchProgressBar.visibility = View.GONE
            }

            SearchFragmentItemsVis.LOADING -> {
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
        if (textSearch != null)
            outState.putString(textOfSearch, textSearch)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textSearch = null
        _binding = null
        searchJob = null
    }

    companion object {
        const val DEBOUNCE_DELAY = 500L
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val STATE_HISTORY_SHOW = "History"
    }
}

enum class SearchFragmentItemsVis {
    CONNECTION_ERROR,
    EMPTY_SEARCH,
    SUCCESS,
    SHOW_HISTORY,
    START_VIEW,
    LOADING
}
