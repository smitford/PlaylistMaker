package com.example.playlistmaker.ui.player

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.use_cases.PlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PlayerViewModel(private val playerInteractor: PlayerInteractor) :
    ViewModel() {
    private val handler = Handler(Looper.getMainLooper())
    private var playerFragmentState =
        MutableLiveData<PlayerFragmentState>()
    private var playerJob: Job? = null

    init {
        playerFragmentState.value = PlayerFragmentState(
            playerState = STATE_DEFAULT,
            timeCode = R.string.play_time.toString()
        )
    }

    override fun onCleared() {
        super.onCleared()
        stop()
        release()
        handler.removeCallbacksAndMessages(null)
    }

    private fun stop() = playerInteractor.stop()
    private fun release() = playerInteractor.release()

    fun prepare(track: Track) {
        try {
            prepareMediaPlayer(track.previewUrl)
        } catch (e: Exception) {
            playerFragmentState.value = getCurrentStatus()
        }
    }

    fun getPlayerState(): LiveData<PlayerFragmentState> = playerFragmentState


    private fun prepareMediaPlayer(url: String) {
        playerInteractor.prepare(url)
        playerFragmentState.value = getCurrentStatus().copy(playerState = STATE_PREPARED)
    }

    private fun getCurrentStatus(): PlayerFragmentState =
        playerFragmentState.value ?: PlayerFragmentState(
            playerState = STATE_DEFAULT, timeCode = "00:00"
        )

    private fun startMediaPlayer() {
        playerInteractor.start()
        playerFragmentState.value = getCurrentStatus().copy(playerState = STATE_PLAYING)
        playerTimeRefresher()
    }

    fun pauseMediaPlayer() {
        playerInteractor.pause()
        playerJob?.cancel()
        playerFragmentState.value = getCurrentStatus().copy(playerState = STATE_PAUSED)
    }

    private fun playerTimeRefresher() {
        playerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {

                playerFragmentState.value =
                    getCurrentStatus().copy(
                        timeCode =
                        playerInteractor.getPosition()
                    )
                delay(PLAY_DEBOUNCE_DELAY)
            }
            playerFragmentState.value =
                PlayerFragmentState(
                    playerState = STATE_PREPARED, timeCode = "00:00"
                )

        }
    }

    fun playbackControl() {
        when (playerFragmentState.value?.playerState) {
            STATE_PLAYING -> pauseMediaPlayer()
            STATE_PREPARED, STATE_PAUSED -> startMediaPlayer()
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val PLAY_DEBOUNCE_DELAY = 250L
    }

}