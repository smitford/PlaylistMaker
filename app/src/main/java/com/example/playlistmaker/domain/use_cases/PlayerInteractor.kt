package com.example.playlistmaker.domain.use_cases

import com.example.playlistmaker.domain.api.PlayerRepository

class PlayerInteractor(private val repository: PlayerRepository) {
    fun prepare(url: String)= repository.prepareMediaPlayer(url = url)
    fun pause() = repository.pauseMediaPlayer()
    fun start()= repository.startMediaPlayer()
    fun release() = repository.releaseMediaPlayer()
    fun getPosition(): Int = repository.getPosition()
    fun isPlaying() : Boolean = repository.isPlaying()
}
