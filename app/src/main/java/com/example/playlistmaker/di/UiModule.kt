package com.example.playlistmaker.di

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.PlayerViewModel
import com.example.playlistmaker.ui.search.SearchTrackViewModel
import com.example.playlistmaker.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModules = module {

    viewModel<PlayerViewModel> { (track: Track) ->
        PlayerViewModel(playerInteractor = get(), track = track)

    }
    viewModel<SearchTrackViewModel> {
        SearchTrackViewModel(
            trackSearchUseCase = get(),
            trackSaveUseCase = get(),
            trackGetUseCase = get(),
            trackClearHistoryUseCase = get()
        )
    }
    viewModel<SettingsViewModel> {
        SettingsViewModel(themeInteractor = get())
    }
}

