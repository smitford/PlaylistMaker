package com.example.playlistmaker.domain.use_cases

interface PlayerInteractor {
    fun prepare(url: String)
    fun pause()
    fun start()
    fun release()
    fun getPosition(): Int
    fun isPlaying(): Boolean
    fun stop()
}