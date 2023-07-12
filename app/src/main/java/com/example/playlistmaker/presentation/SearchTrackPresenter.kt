package com.example.playlistmaker.presentation

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TrackNetworkRepositoryImp
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.consumer.DataConsumer
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.use_cases.TrackSearchUseCase
import com.example.playlistmaker.ui.search.SearchActItemsVisib
import com.example.playlistmaker.ui.search.SearchActivity

class SearchTrackPresenter(private val view: SearchActivity) {

    private val handler = Handler(Looper.getMainLooper())
    private var currentConsumeRunnable: Runnable? = null

    private val retrofitNetworkClient by lazy(LazyThreadSafetyMode.NONE) { RetrofitNetworkClient() }

    private val repository by lazy(LazyThreadSafetyMode.NONE) {
        TrackNetworkRepositoryImp(
            retrofitNetworkClient
        )
    }
    private val trackSearchUseCase by lazy(LazyThreadSafetyMode.NONE) {
        TrackSearchUseCase(
            repository = repository
        )
    }

   fun searchTrack(term: String) {

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
            view.recyclerViewSongs.adapter =view.adapterSearch
            when(result){
                is DataConsumer.Success -> {
                    view.adapterSearch.tracks = result.data.toMutableList()
                    view.recyclerViewSongs.adapter?.notifyDataSetChanged()
                    if (result.data.isNotEmpty()) {
                        view.errorVisibility(SearchActItemsVisib.SUCCESS)
                    } else view.errorVisibility(SearchActItemsVisib.EMPTY_SEARCH)
                }
                is DataConsumer.Error -> view.errorVisibility(SearchActItemsVisib.CONNECTION_ERROR)
            }
        }
    }

    fun onDestroy() {
        handler.removeCallbacksSafe(currentConsumeRunnable)
    }

    private fun Handler.removeCallbacksSafe(r: Runnable?) {
        r?.let {
            removeCallbacks(r)
        }
    }

}
