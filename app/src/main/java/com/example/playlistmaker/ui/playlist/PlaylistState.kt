package com.example.playlistmaker.ui.playlist

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.PlaylistInfo

sealed interface PlaylistState {
    data class EmptyPlaylist(val playlistInfo: PlaylistInfo): PlaylistState
    data class FilledPlaylist(val playlist: Playlist, val playingTime: Int) :PlaylistState
}