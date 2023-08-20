package com.example.playlistmaker.ui.player

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.use_cases.PlayerInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val playerInteractor: PlayerInteractor) :
    ViewModel() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val PLAY_DEBOUNCE_DELAY = 250L
    }

    private val handler = Handler(Looper.getMainLooper())

    private var playerFragmentState =
        MutableLiveData<PlayerFragmentState>()

    init {
        playerFragmentState.value = PlayerFragmentState(
            playerState = STATE_DEFAULT,
            timeCode = R.string.play_time.toString()
        )
    }

    override fun onCleared() {
        super.onCleared()
        stop ()
        release()
        handler.removeCallbacksAndMessages(null)
    }

    private fun stop () = playerInteractor.stop()
    private fun release () = playerInteractor.release()

    fun prepare(track: Track) {
        try {
            prepareMediaPlayer(track.previewUrl)
        } catch (e: Exception) {
            playerFragmentState.value =getCurrentStatus()
        }
    }
    fun getPlayerState(): LiveData<PlayerFragmentState> = playerFragmentState


    private fun prepareMediaPlayer(url: String) {
        playerInteractor.prepare(url)
        playerFragmentState.value = getCurrentStatus().copy(playerState = STATE_PREPARED)
    }

    private fun getCurrentStatus(): PlayerFragmentState =
        playerFragmentState.value ?: PlayerFragmentState(
            playerState = STATE_DEFAULT, timeCode = R.string.play_time.toString()
        )

    private fun startMediaPlayer() {
        playerInteractor.start()
        playerFragmentState.value = getCurrentStatus().copy(playerState = STATE_PLAYING)
        handler.post(playerTimeRefresher())
    }

    fun pauseMediaPlayer() {
        playerInteractor.pause()
        playerFragmentState.value = getCurrentStatus().copy(playerState = STATE_PAUSED)
    }

    private fun playerTimeRefresher(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerInteractor.isPlaying()) {
                    playerFragmentState.value =
                        getCurrentStatus().copy(
                            timeCode =
                            SimpleDateFormat(
                                "mm:ss",
                                Locale.getDefault()
                            ).format(playerInteractor.getPosition())
                        )
                    handler.postDelayed(this, PLAY_DEBOUNCE_DELAY)
                } else {
                    playerFragmentState.value =
                        PlayerFragmentState(
                            playerState = STATE_PREPARED, timeCode = R.string.play_time.toString()
                        )
                }
            }
        }
    }

    fun playbackControl() {
        when (playerFragmentState.value?.playerState) {
            STATE_PLAYING -> pauseMediaPlayer()
            STATE_PREPARED, STATE_PAUSED -> startMediaPlayer()
        }
    }

}