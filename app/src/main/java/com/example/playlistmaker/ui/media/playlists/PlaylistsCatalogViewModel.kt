package com.example.playlistmaker.ui.media.playlists

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.PlaylistInfo
import com.example.playlistmaker.domain.use_cases.DataBasePlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistsCatalogViewModel(val dataBasePlaylist: DataBasePlaylistInteractor) : ViewModel() {

    private var playlistCatalogState= MutableLiveData<PlaylistCatalogState>()

    fun getState() = playlistCatalogState

    private fun changeStatus(result: List<PlaylistInfo>?) =
        if (!result.isNullOrEmpty()) {
            Log.d("Status changed", "$result")
            playlistCatalogState.value = PlaylistCatalogState.LoadedCatalog(result)
        } else {
            Log.d("Status changed null", "$result")
            playlistCatalogState.value = PlaylistCatalogState.Empty
        }

    fun loadPlaylistCatalog() {
        viewModelScope.launch {
            dataBasePlaylist.getPlaylistsInfo().collect { result ->
                changeStatus(result)
            }
        }
    }


}