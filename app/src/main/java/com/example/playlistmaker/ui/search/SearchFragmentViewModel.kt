package com.example.playlistmaker.ui.search

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.consumer.DataConsumer
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.use_cases.TrackClearHistoryUseCase
import com.example.playlistmaker.domain.use_cases.TrackGetUseCase
import com.example.playlistmaker.domain.use_cases.TrackSaveUseCase
import com.example.playlistmaker.domain.use_cases.TrackSearchUseCase

class SearchFragmentViewModel(
    private val trackClearHistoryUseCase: TrackClearHistoryUseCase,
    private val trackGetUseCase: TrackGetUseCase,
    private val trackSaveUseCase: TrackSaveUseCase,
    private val trackSearchUseCase: TrackSearchUseCase
) : ViewModel() {
    private var searchFragmentState = MutableLiveData<SearchFragmentState>()
    private val handler = Handler(Looper.getMainLooper())
    private var currentConsumeRunnable: Runnable? = null

    init {
        searchFragmentState.value = SearchFragmentState.Start
    }

    fun clearHistory() {
        trackClearHistoryUseCase.execute()
        searchFragmentState.value = SearchFragmentState.Start
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
        searchFragmentState.value = SearchFragmentState.Loading
        trackSearchUseCase.searchTracks(
            term = term,
            consumer = object : Consumer<List<Track>> {
                override fun consume(data: DataConsumer<List<Track>>) {
                    handler.removeCallbacksSafe(currentConsumeRunnable)
                    val consumeRunnable = getSearchRunnable(data)
                    currentConsumeRunnable = consumeRunnable
                    handler.post(consumeRunnable)
                }
            })
    }

    private fun getSearchRunnable(result: DataConsumer<List<Track>>): Runnable {
        return Runnable {
            when (result) {
                is DataConsumer.Success -> {
                    if (result.data.isNotEmpty()) {
                        searchFragmentState.value = SearchFragmentState.State(
                            trackList = result.data, STATE_SEARCH_RESULT_SHOW
                        )
                    } else {
                        searchFragmentState.value = SearchFragmentState.InvalidRequest
                    }
                }

                is DataConsumer.Error -> {
                    searchFragmentState.value = SearchFragmentState.ConnectionError
                }
            }
        }
    }

    fun getSearchActivityState(): LiveData<SearchFragmentState> =
        searchFragmentState

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksSafe(currentConsumeRunnable)
    }

    private fun Handler.removeCallbacksSafe(r: Runnable?) {
        r?.let {
            removeCallbacks(r)
        }
    }

    companion object {
        const val STATE_HISTORY_SHOW = "History"
        const val STATE_SEARCH_RESULT_SHOW = "Result"
    }
}
