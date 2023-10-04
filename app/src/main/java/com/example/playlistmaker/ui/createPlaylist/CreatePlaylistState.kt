package com.example.playlistmaker.ui.createPlaylist


data class CreatePlaylistState(
    var uri: String? = null,
    var playlistName: String = " ",
    var description: String? = null,
    var state: AllStates = AllStates.START
)
