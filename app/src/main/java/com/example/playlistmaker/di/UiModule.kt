package com.example.playlistmaker.di

import com.example.playlistmaker.ui.createPlaylist.CreatePlaylistViewModel
import com.example.playlistmaker.ui.media.favorite_tracks.FavoriteTracksViewModel
import com.example.playlistmaker.ui.media.playlists.PlaylistsListViewModel
import com.example.playlistmaker.ui.player.PlayerViewModel
import com.example.playlistmaker.ui.search.SearchViewModel
import com.example.playlistmaker.ui.settings.SettingsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModules = module {

    viewModel<PlayerViewModel> {
        PlayerViewModel(
            playerInteractor = get(),
            dataBase = get()
        )
    }
    viewModel<SearchViewModel> {
        SearchViewModel(
            trackSearchUseCase = get(),
            trackSaveUseCase = get(),
            trackGetUseCase = get(),
            trackClearHistoryUseCase = get()
        )
    }
    viewModel<SettingsFragmentViewModel> {
        SettingsFragmentViewModel(themeInteractor = get())
    }
    viewModel<FavoriteTracksViewModel>() {
        FavoriteTracksViewModel(dataBase = get())
    }
    viewModel<PlaylistsListViewModel>() {
        PlaylistsListViewModel()
    }

    viewModel<CreatePlaylistViewModel>() {
        CreatePlaylistViewModel(dataBasePlaylistInteractor = get())
    }
}

