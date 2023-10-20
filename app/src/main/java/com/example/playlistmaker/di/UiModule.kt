package com.example.playlistmaker.di

import com.example.playlistmaker.domain.use_cases.SaveImageUseCase
import com.example.playlistmaker.ui.createPlaylist.CreatePlaylistViewModel
import com.example.playlistmaker.ui.editPlaylist.EditPlaylistViewModel
import com.example.playlistmaker.ui.media.favorite_tracks.FavoriteTracksViewModel
import com.example.playlistmaker.ui.media.playlists.PlaylistsCatalogViewModel
import com.example.playlistmaker.ui.player.PlayerViewModel
import com.example.playlistmaker.ui.playlist.PlaylistViewModel
import com.example.playlistmaker.ui.search.SearchViewModel
import com.example.playlistmaker.ui.settings.SettingsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModules = module {

    viewModel<PlayerViewModel> {
        PlayerViewModel(
            playerInteractor = get(),
            dataBaseTrack = get(),
            dataBasePlaylist = get()
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
    viewModel<PlaylistsCatalogViewModel>() {
        PlaylistsCatalogViewModel(dataBasePlaylist = get())
    }

    viewModel<CreatePlaylistViewModel>() {
        CreatePlaylistViewModel(dataBasePlaylistInteractor = get(), saveUseCase = get())
    }
    viewModel<PlaylistViewModel>() {
        PlaylistViewModel(dataBasePlaylistInteractor = get())
    }
    viewModel<EditPlaylistViewModel>() {
        EditPlaylistViewModel(
            playlistId = get(),
            dataBasePlaylistInteractor = get(),
            saveImgUseCase = get(),
            deleteImgUseCase = get()
        )
    }
}

