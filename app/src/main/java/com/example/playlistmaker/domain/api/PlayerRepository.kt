package com.example.playlistmaker.domain.api

interface PlayerRepository {
    fun prepareMediaPlayer(url: String)
    fun pauseMediaPlayer()
    fun startMediaPlayer()
    fun releaseMediaPlayer()
    fun getPosition() : Int
    fun stop()
    fun isPlaying() : Boolean

}