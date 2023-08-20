package com.example.playlistmaker.data.player


import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.PlayerRepository


class PlayerRepositoryImp : PlayerRepository {

    private var _player : MediaPlayer? = null
    private val player get() = _player!!
    private var completion = true
    override fun prepareMediaPlayer(url: String) {
        _player = MediaPlayer()
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener {
            completion = true
        }
        player.setOnCompletionListener {
            completion = false
        }
    }

    override fun stop() {
        player.stop()
    }

    override fun startMediaPlayer() {
        completion = true
        player.start()
    }

    override fun pauseMediaPlayer() {
        player.pause()
    }

    override fun releaseMediaPlayer() {

        player.release()
        _player=null

    }

    override fun getPosition(): Int = player.currentPosition

    override fun isPlaying(): Boolean = completion
}