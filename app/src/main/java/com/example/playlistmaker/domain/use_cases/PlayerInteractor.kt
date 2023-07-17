package com.example.playlistmaker.domain.use_cases

import com.example.playlistmaker.domain.api.PlayerRepository

class PlayerInteractor(private val repository: PlayerRepository) {
    fun prepare(url: String): Int = repository.prepareMediaPlayer(url = url)
    fun pause(): Int = repository.pauseMediaPlayer()
    fun startMediaPlayer(): Int = repository.startMediaPlayer()
    fun release() = repository.releaseMediaPlayer()
    fun getPosition(): Int = repository.getPosition()
}
