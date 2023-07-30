package com.example.playlistmaker.domain.use_cases.implementation

import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.use_cases.PlayerInteractor

class PlayerInteractorImp(private val repository: PlayerRepository) : PlayerInteractor {
    override fun prepare(url: String)= repository.prepareMediaPlayer(url = url)
    override fun pause() = repository.pauseMediaPlayer()
    override fun start()= repository.startMediaPlayer()
    override fun release() = repository.releaseMediaPlayer()
    override fun getPosition(): Int = repository.getPosition()
    override fun isPlaying() : Boolean = repository.isPlaying()
}
