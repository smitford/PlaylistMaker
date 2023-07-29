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

class SearchTrackViewModel(
    private val trackClearHistoryUseCase: TrackClearHistoryUseCase,
    private val trackGetUseCase: TrackGetUseCase,
    private val trackSaveUseCase: TrackSaveUseCase,
    private val trackSearchUseCase: TrackSearchUseCase
) : ViewModel() {
    companion object {
        const val STATE_HISTORY_SHOW = "History"
        const val STATE_SEARCH_RESULT_SHOW = "Result"
    }

    private var searchActivityState = MutableLiveData<SearchActivityState>()
    private val handler = Handler(Looper.getMainLooper())
    private var currentConsumeRunnable: Runnable? = null

    init {
        searchActivityState.value = SearchActivityState.Start
    }

    fun clearHistory() {
        trackClearHistoryUseCase.execute()
        searchActivityState.value = SearchActivityState.Start
    }

    fun saveTrack(track: Track) {
        trackSaveUseCase.execute(track)
    }

    fun refreshHistory() {
        if (trackGetUseCase.execute().isEmpty()) {
            searchActivityState.value = SearchActivityState.Start
        } else {
            searchActivityState.value = SearchActivityState.State(
                trackList = trackGetUseCase.execute().toMutableList(), STATE_HISTORY_SHOW
            )
        }
    }

    fun searchTrack(term: String) {
        searchActivityState.value = SearchActivityState.Loading
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
                        searchActivityState.value = SearchActivityState.State(
                            trackList = result.data, STATE_SEARCH_RESULT_SHOW
                        )
                    } else {
                        searchActivityState.value = SearchActivityState.InvalidRequest
                    }
                }
                is DataConsumer.Error -> {
                    searchActivityState.value = SearchActivityState.ConnectionError
                }
            }
        }
    }

    fun getSearchActivityState(): LiveData<SearchActivityState> =
        searchActivityState

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksSafe(currentConsumeRunnable)
    }

    private fun Handler.removeCallbacksSafe(r: Runnable?) {
        r?.let {
            removeCallbacks(r)
        }
    }
}
