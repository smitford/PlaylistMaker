package com.example.playlistmaker.ui.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator

class SearchViewModelFactory(private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        SearchTrackViewModel(
            trackClearHistoryUseCase = Creator.getTrackClearHistoryUseCase(context = context),
            trackGetUseCase = Creator.getTrackGetUseCase(context = context),
            trackSaveUseCase = Creator.getTrackSaveUseCase(context = context),
            trackSearchUseCase = Creator.getTrackSearchUseCase(context = context)
        ) as T

}