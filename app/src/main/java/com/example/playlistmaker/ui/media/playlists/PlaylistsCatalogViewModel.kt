package com.example.playlistmaker.ui.media.playlists

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.PlaylistInfo
import com.example.playlistmaker.domain.use_cases.DataBasePlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistsCatalogViewModel(val dataBasePlaylist: DataBasePlaylistInteractor) : ViewModel() {

    private lateinit var playlistCatalogState: MutableLiveData<PlaylistCatalogState>

    init {
        loadPlaylistCatalog()
    }

    fun getState(): MutableLiveData<PlaylistCatalogState> = playlistCatalogState

    private fun changeStatus(result: List<PlaylistInfo>?) =
        if (result != null)
            playlistCatalogState = MutableLiveData(PlaylistCatalogState.LoadedCatalog(result))
        else
            playlistCatalogState = MutableLiveData(PlaylistCatalogState.Empty)


    private fun loadPlaylistCatalog() {
        viewModelScope.launch {
            dataBasePlaylist.getPlaylistsInfo().collect { result ->
                changeStatus(result)
            }
        }
    }


}