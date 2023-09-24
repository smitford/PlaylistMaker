package com.example.playlistmaker.ui.createPlaylist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.use_cases.DataBasePlaylistInteractor
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(val dataBasePlaylistInteractor: DataBasePlaylistInteractor) :
    ViewModel() {

    private var createPlaylistState = MutableLiveData<CreatePlaylistState>()
    private fun changeState(data: String, newState: AllStates) {
        when (newState) {
            AllStates.SAVED_IMG -> createPlaylistState.value =
                getCurrentStatus()?.apply { this.uri = data; this.state = AllStates.SAVED_IMG }

            AllStates.SAVED_NAME -> createPlaylistState.value =
                getCurrentStatus()?.apply {
                    this.playlistName = data; this.state = AllStates.SAVED_NAME
                }

            AllStates.SAVED_DESCRIPTION -> createPlaylistState.value =
                getCurrentStatus()?.apply {
                    this.description = data; this.state = AllStates.SAVED_DESCRIPTION
                }

            AllStates.SAVED_PLAYLIST -> createPlaylistState.value =
                getCurrentStatus()?.copy(state = AllStates.SAVED_PLAYLIST)

            else -> createPlaylistState.value = getCurrentStatus()
        }
    }

    private fun getCurrentStatus() = createPlaylistState.value

    fun getCurrentData() = createPlaylistState

    fun saveImg(imgUri: String) {
        changeState(imgUri, AllStates.SAVED_IMG)
    }

    fun saveName(playlistName: String) {
        changeState(playlistName, AllStates.SAVED_NAME)
    }

    fun saveDescription(playlistDescription: String) {
        changeState(playlistDescription, AllStates.SAVED_DESCRIPTION)
    }

    fun savePlaylist() {
        val data = getCurrentStatus()
        viewModelScope.launch {
            if (data != null) {
                dataBasePlaylistInteractor.createPlaylist(
                    playlistName = data.playlistName,
                    playlistDescription = data.description,
                    imgUri = data.uri
                )
            }
        }
    }

}



