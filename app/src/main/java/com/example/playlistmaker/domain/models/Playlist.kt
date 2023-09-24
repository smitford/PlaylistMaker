package com.example.playlistmaker.domain.models

data class Playlist(
    val id: Int,
    val name: String,
    val description: String,
    val imgUri: String,
    val tracks: List<Track>
)