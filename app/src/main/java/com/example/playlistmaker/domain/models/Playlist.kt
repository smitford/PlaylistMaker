package com.example.playlistmaker.domain.models

data class Playlist(
    val playlistInfo: PlaylistInfo,
    val tracks: List<Track>
)