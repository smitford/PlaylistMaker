package com.example.playlistmaker.domain.api

interface PlayerRepository {
    fun prepareMediaPlayer(url: String) : Int
    fun pauseMediaPlayer() : Int
    fun startMediaPlayer() : Int
    fun releaseMediaPlayer()
    fun getPosition() : Int

}