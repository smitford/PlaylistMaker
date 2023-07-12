package com.example.playlistmaker.data.player


import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.PlayerRepository


class PlayerRepositoryImp : PlayerRepository {
    companion object {
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var player = MediaPlayer()


    override fun prepareMediaPlayer(url: String) :Int{
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener {
        }
        player.setOnCompletionListener {
        }
        return STATE_PREPARED
    }

    override fun startMediaPlayer() : Int {
        player.start()
        return STATE_PLAYING
    }

    override fun pauseMediaPlayer() :Int {
        player.pause()
        return STATE_PAUSED
    }

    override fun releaseMediaPlayer(){
        player.release()
    }

    override fun getPosition(): Int = player.currentPosition
}