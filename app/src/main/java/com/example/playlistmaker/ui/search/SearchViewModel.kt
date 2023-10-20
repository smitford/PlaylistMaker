package com.example.playlistmaker.ui.search


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.use_cases.TrackClearHistoryUseCase
import com.example.playlistmaker.domain.use_cases.TrackGetUseCase
import com.example.playlistmaker.domain.use_cases.TrackSaveUseCase
import com.example.playlistmaker.domain.use_cases.TrackSearchUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackClearHistoryUseCase: TrackClearHistoryUseCase,
    private val trackGetUseCase: TrackGetUseCase,
    private val trackSaveUseCase: TrackSaveUseCase,
    private val trackSearchUseCase: TrackSearchUseCase
) : ViewModel() {
    private var searchFragmentState = MutableLiveData<SearchFragmentState>()

    private var searchJob: Job? = null


    init {
        searchFragmentState.value = SearchFragmentState.Start
    }

    fun clearHistory() {
        trackClearHistoryUseCase.execute()
        searchFragmentState.value = SearchFragmentState.Start
    }

    fun stopSearch() {
        searchJob?.cancel()
    }

    fun saveTrack(track: Track) {
        trackSaveUseCase.execute(track)
    }

    fun refreshHistory() {
        if (trackGetUseCase.execute().isEmpty()) {
            searchFragmentState.value = SearchFragmentState.Start
        } else {
            searchFragmentState.value = SearchFragmentState.State(
                trackList = trackGetUseCase.execute().toMutableList(), STATE_HISTORY_SHOW
            )
        }
    }

    fun searchTrack(term: String) {
        if (term.isBlank()) return
        searchFragmentState.value = SearchFragmentState.Loading
        searchJob = viewModelScope.launch {
            trackSearchUseCase.searchTracks(term = term).collect { list ->
                getSearchRunnable(list)
            }
        }
    }

    private fun getSearchRunnable(result: List<Track>?) {

        if (result == null) {
            searchFragmentState.value = SearchFragmentState.ConnectionError
        } else {
            if (result.isNotEmpty()) {
                searchFragmentState.value = SearchFragmentState.State(
                    trackList = result, STATE_SEARCH_RESULT_SHOW
                )
            } else {
                searchFragmentState.value = SearchFragmentState.InvalidRequest
            }
        }
    }

    fun getSearchActivityState(): LiveData<SearchFragmentState> =
        searchFragmentState

    companion object {
        const val STATE_HISTORY_SHOW = "History"
        const val STATE_SEARCH_RESULT_SHOW = "Result"
    }
}
