package com.example.playlistmaker.ui.player

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.PlaylistInfo
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.use_cases.DataBasePlaylistInteractor
import com.example.playlistmaker.domain.use_cases.DataBaseTrackInteractor
import com.example.playlistmaker.domain.use_cases.PlayerInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    val dataBaseTrack: DataBaseTrackInteractor,
    val dataBasePlaylist: DataBasePlaylistInteractor
) :
    ViewModel() {
    private var playerFragmentState =
        MutableLiveData<PlayerFragmentState>()
    private var playerJob: Job? = null
    private var isFavorite = false
    private var addTrackToPlaylistStatus: MutableLiveData<Boolean?> = MutableLiveData(null)
    private var playlistCatalogState: MutableLiveData<List<PlaylistInfo>?> = MutableLiveData(null)


    init {
        playerFragmentState.value = PlayerFragmentState(
            playerState = STATE_DEFAULT,
            timeCode = R.string.play_time.toString(),
            isFavorite = isFavorite
        )
    }

    override fun onCleared() {
        super.onCleared()
        stop()
        release()
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
            playerState = STATE_DEFAULT,
            timeCode = R.string.play_time.toString(),
            isFavorite = isFavorite
        )

    fun checkForFavorite(trackID: Int) {
        viewModelScope.launch {
            dataBaseTrack.isTrackFavorite(trackID = trackID).collect { isFav ->
                changeStatus(isFav)
            }
        }
    }

    private fun changeStatus(status: Boolean) {
        playerFragmentState.value = getCurrentStatus().copy(isFavorite = status)
    }


    private fun startMediaPlayer() {
        playerInteractor.start()
        playerFragmentState.value = getCurrentStatus().copy(playerState = STATE_PLAYING)
        playerTimeRefresher()
    }

    fun changeStatus(track: Track) {
        if (isFavorite)
            removeTrackFromFav(track = track)
        else
            addTrackToFav(track = track)
    }

    private fun addTrackToFav(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            dataBaseTrack.saveFavoriteTrack(track = track)
        }
        isFavorite = true
        playerFragmentState.value = getCurrentStatus().copy(isFavorite = isFavorite)
    }

    fun addTrackToPlaylist(track: Track, playlistPK: Int) {
        Log.d("Saved in playlist", "$playlistPK")
        addTrackToPlaylistStatus.value = null
        viewModelScope.launch {
            saveTrack(track = track)
            dataBasePlaylist.addTrackToPlaylist(trackPK = track.trackId, playlistPK = playlistPK)
                .collect { result ->
                    addToPlaylistStatus(result = result)
                }
        }

    }

    private suspend fun saveTrack(track: Track) {
        dataBaseTrack.saveTrack(track = track)

    }


    fun getPlayerName(playlistPK: Int) = playlistCatalogState.value?.get(playlistPK)?.name

    private fun addToPlaylistStatus(result: Boolean) {
        Log.d("If there duplicates", "$result")
        addTrackToPlaylistStatus.value = result
    }

    fun getAddStatus() = addTrackToPlaylistStatus

    private fun removeTrackFromFav(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            dataBaseTrack.deleteTrackFromFav(trackID = track.trackId)
        }
        isFavorite = false
        playerFragmentState.value = getCurrentStatus().copy(isFavorite = isFavorite)

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
                    playerState = STATE_PREPARED,
                    timeCode = R.string.play_time.toString(),
                    isFavorite = isFavorite
                )

        }
    }

    fun playbackControl() {
        when (playerFragmentState.value?.playerState) {
            STATE_PLAYING -> pauseMediaPlayer()
            STATE_PREPARED, STATE_PAUSED -> startMediaPlayer()
        }
    }

    fun loadPlaylistCatalog() {
        viewModelScope.launch {
            dataBasePlaylist.getPlaylistsInfo().collect { result ->
                fillList(result = result)
            }
        }
    }

    private fun fillList(result: List<PlaylistInfo>?) {
        playlistCatalogState.value = result
        Log.d("Loaded Catalog", "$result")
    }

    fun getPlaylistCatalogState() = playlistCatalogState

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val PLAY_DEBOUNCE_DELAY = 300L
    }

}