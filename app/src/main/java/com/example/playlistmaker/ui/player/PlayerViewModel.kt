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

    private var playerActivityState =
        MutableLiveData<PlayerActivityState>()

    init {
        playerActivityState.value = PlayerActivityState(
            playerState = STATE_DEFAULT,
            timeCode = R.string.play_time.toString()
        )
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        handler.removeCallbacksAndMessages(null)
    }

    fun prepare(track: Track) {
        try {
            prepareMediaPlayer(track?.previewUrl.toString())
            Log.d("Release","Done")
        } catch (e: Exception) {
            playerActivityState.value =getCurrentStatus()
        }
    }
    fun getPlayerState(): LiveData<PlayerActivityState> = playerActivityState


    private fun prepareMediaPlayer(url: String) {
        playerInteractor.prepare(url)
        playerActivityState.value = getCurrentStatus().copy(playerState = STATE_PREPARED)
    }

    private fun getCurrentStatus(): PlayerActivityState =
        playerActivityState.value ?: PlayerActivityState(
            playerState = STATE_DEFAULT, timeCode = R.string.play_time.toString()
        )

    private fun startMediaPlayer() {
        playerInteractor.start()
        playerActivityState.value = getCurrentStatus().copy(playerState = STATE_PLAYING)
        handler.post(playerTimeRefresher())
    }

    fun pauseMediaPlayer() {
        playerInteractor.pause()
        playerActivityState.value = getCurrentStatus().copy(playerState = STATE_PAUSED)
    }

    private fun playerTimeRefresher(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerInteractor.isPlaying()) {
                    playerActivityState.value =
                        getCurrentStatus().copy(
                            timeCode =
                            SimpleDateFormat(
                                "mm:ss",
                                Locale.getDefault()
                            ).format(playerInteractor.getPosition())
                        )
                    handler.postDelayed(this, PLAY_DEBOUNCE_DELAY)
                } else {
                    playerActivityState.value =
                        PlayerActivityState(
                            playerState = STATE_PREPARED, timeCode = R.string.play_time.toString()
                        )
                }
            }
        }
    }

    fun playbackControl() {
        when (playerActivityState.value?.playerState) {
            STATE_PLAYING -> pauseMediaPlayer()
            STATE_PREPARED, STATE_PAUSED -> startMediaPlayer()
        }
    }

}