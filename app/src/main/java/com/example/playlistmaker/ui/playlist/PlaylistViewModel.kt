package com.example.playlistmaker.ui.playlist

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.use_cases.DataBasePlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class PlaylistViewModel(val dataBasePlaylistInteractor: DataBasePlaylistInteractor) : ViewModel() {
    private val state = MutableLiveData<PlaylistState>()

    fun getState() = state

    fun getPlaylist(playlistId: Int) {
        viewModelScope.launch {
            dataBasePlaylistInteractor.getPlaylist(playlistPK = playlistId).collect { playlist ->
                changeState(playlist = playlist)
            }
        }
    }

    private fun changeState(playlist: Playlist) {
        if (playlist.playlistInfo.tracksNumber == 0)
            state.value = PlaylistState.EmptyPlaylist(playlistInfo = playlist.playlistInfo)
        else {
            state.value = PlaylistState.FilledPlaylist(
                playlist = playlist,
                playingTime = calculatePlayingTime(playlistsTracks = playlist.tracks)
            )
        }
    }

    private fun calculatePlayingTime(playlistsTracks: List<Track>): Int =
        SimpleDateFormat("mm", Locale.getDefault()).format(playlistsTracks.sumOf {
            it.trackTimeMillis.toInt()
        }).toInt()

    fun deleteTrackFromPlaylist(trackId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataBasePlaylistInteractor.deleteTrackFromPlaylist(
                playlistPK = getPlaylistId(),
                trackPK = trackId
            )
        }
        getPlaylist(getPlaylistId())
    }

    private fun getPlaylistId() =
        when (state.value) {
            is PlaylistState.FilledPlaylist -> (state.value as PlaylistState.FilledPlaylist).playlist.playlistInfo.id
            is PlaylistState.EmptyPlaylist -> (state.value as PlaylistState.EmptyPlaylist).playlistInfo.id
            else -> 0
        }

    fun deletePlaylist(playlistId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataBasePlaylistInteractor.deletePlaylist(playlistPK = playlistId)
        }

    fun shearPlaylist(context: Context) {
        val shearIntent = Intent(Intent.ACTION_SEND)
        shearIntent.data = Uri.parse("mailto:")
        shearIntent.putExtra(Intent.EXTRA_TEXT, messageTextCreator())
        try {
            context.startActivity(shearIntent)
        } catch (e: Exception) {
        }
    }

    private fun messageTextCreator(): String {
        val playlist = state.value as PlaylistState.FilledPlaylist
        var massage = ""
        val tracksList =
            playlist.playlist.tracks.map {
                it.artistName + " - " + it.trackName + " " + modifyFormat(
                    it.trackTimeMillis
                )
            }
        tracksList.toTypedArray()
            .forEachIndexed { index, s -> massage += "${index + 1} $s\n" }


        return playlist.playlist.playlistInfo.let {
            it.name + "\n" + it.description + "\n" + it.tracksNumber + " треков" +
                    "\n" + massage
        }
    }

    private fun modifyFormat(time: String) = SimpleDateFormat(
        "mm:ss",
        Locale.getDefault()
    ).format(time.toLong())
}


