package com.example.playlistmaker.di

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.media.FavoriteTracksFragmentVm
import com.example.playlistmaker.ui.media.PlaylistFragmentViewModel
import com.example.playlistmaker.ui.player.PlayerViewModel
import com.example.playlistmaker.ui.search.SearchFragmentViewModel
import com.example.playlistmaker.ui.settings.SettingsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModules = module {

    viewModel<PlayerViewModel> { (track: Track) ->
        PlayerViewModel(playerInteractor = get(), track = track)

    }
    viewModel<SearchFragmentViewModel> {
        SearchFragmentViewModel(
            trackSearchUseCase = get(),
            trackSaveUseCase = get(),
            trackGetUseCase = get(),
            trackClearHistoryUseCase = get()
        )
    }
    viewModel<SettingsFragmentViewModel> {
        SettingsFragmentViewModel(themeInteractor = get())
    }
    viewModel<FavoriteTracksFragmentVm>() {
        FavoriteTracksFragmentVm()
    }
    viewModel<PlaylistFragmentViewModel>(){
        PlaylistFragmentViewModel()
    }
}

