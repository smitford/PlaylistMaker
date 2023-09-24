package com.example.playlistmaker.ui.createPlaylist


data class CreatePlaylistState(
    var uri: String = "",
    var playlistName: String = "",
    var description: String = "",
    var state: AllStates = AllStates.START
)
