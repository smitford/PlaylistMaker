package com.example.playlistmaker.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.PlayerActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import handler
import org.koin.androidx.viewmodel.ext.android.viewModel
import textOfSearch
import SEARCH_DEBOUNCE_DELAY as SEARCH_DEBOUNCE_DELAY1


class SearchFragment : Fragment() {
    companion object {
        const val CLEAR_DEBOUNCE_DELAY = 500L
        const val STATE_HISTORY_SHOW = "History"

    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var textSearch: String? = null
    private lateinit var recyclerViewSongs: RecyclerView
    private lateinit var adapterSearch: AdapterSearch
    private lateinit var adapterSearchHistory: AdapterSearchHistory
    private val searchRequest = Runnable { search() }
    private val clear = Runnable { clearSearchField() }
    private val searchFragmentViewModel by viewModel<SearchFragmentViewModel>()

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

        val callBackSearch = fun(track: Track) {
            searchFragmentViewModel.saveTrack(track = track)
            val displayPlayer =
                Intent(requireContext(), PlayerActivity::class.java)
            displayPlayer.putExtra("track", track)
            activity?.let {
                Navigation.findNavController(it.findViewById(R.id.action_searchFragment_to_playerActivity))
                    .handleDeepLink(displayPlayer)

            }
        }

        val callBackHistory = fun(track: Track) {
            val displayPlayer =
                Intent(requireContext(), PlayerActivity::class.java)
            displayPlayer.putExtra("track", track)
            activity?.let {
                Navigation.findNavController(it.findViewById(R.id.action_searchFragment_to_playerActivity))
                    .handleDeepLink(displayPlayer)

            }
        }

        recyclerViewSongs = binding.recyclerViewSongs
        recyclerViewSongs.layoutManager = LinearLayoutManager(requireContext())
        adapterSearch = AdapterSearch(callBackSearch)
        adapterSearchHistory = AdapterSearchHistory(callBackHistory)
        recyclerViewSongs.adapter = adapterSearch

        if (savedInstanceState != null)
            textSearch = savedInstanceState.getString(textOfSearch)

        binding.buttonClearSearchHistory.setOnClickListener {
            searchFragmentViewModel.clearHistory()
        }

        binding.buttonDownloadFail.setOnClickListener { search() }

        binding.clearTextSearch.setOnClickListener {
            handler.removeCallbacks(searchRequest)
            clearDebounce()
        }

        searchFragmentViewModel.getSearchActivityState()
            .observe(viewLifecycleOwner) { searchActivityState ->
                when (searchActivityState) {
                    is SearchFragmentState.Start -> {
                        elementsVisibility(SearchActItemsVis.START_VIEW)
                    }

                    is SearchFragmentState.Loading -> {
                        elementsVisibility(SearchActItemsVis.LOADING)
                    }

                    is SearchFragmentState.ConnectionError -> {
                        elementsVisibility(SearchActItemsVis.CONNECTION_ERROR)
                    }

                    is SearchFragmentState.InvalidRequest -> {
                        elementsVisibility(SearchActItemsVis.EMPTY_SEARCH)
                    }

                    is SearchFragmentState.State -> {
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
                changeVisBottomNav(View.GONE)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearTextSearch.visibility = clearButtonVisibility(s)
                if (binding.searchBar.hasFocus() && binding.searchBar.text.isNullOrBlank()) {
                    searchFragmentViewModel.refreshHistory()
                } else {
                    if (!binding.searchBar.text.isNullOrBlank())
                        searchDebounce()
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
                searchFragmentViewModel.refreshHistory()

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
        searchFragmentViewModel.searchTrack(binding.searchBar.text.toString())

    private fun clearSearchField() {
        binding.searchBar.setText("")
        searchFragmentViewModel.refreshHistory()
    }

    private fun clearDebounce() {
        handler.removeCallbacks(clear)
        handler.postDelayed(clear, CLEAR_DEBOUNCE_DELAY)
        elementsVisibility(SearchActItemsVis.START_VIEW)
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
                binding.imageviewDownloadFail.setImageResource(R.drawable.search_zero)
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
        if (textSearch != null)
            outState.putString(textOfSearch, textSearch)
    }

    fun searchDebounce() {
        handler.removeCallbacks(searchRequest)
        handler.postDelayed(searchRequest, SEARCH_DEBOUNCE_DELAY1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(searchRequest)
        textSearch = null
        _binding = null
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
