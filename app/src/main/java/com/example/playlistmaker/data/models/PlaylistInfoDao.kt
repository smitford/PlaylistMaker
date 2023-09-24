package com.example.playlistmaker.data.models

data class PlaylistInfoDao(
    val id: Int,
    val name: String,
    val description: String,
    val imgUri: String,
    val tracksNumber: Int
)
