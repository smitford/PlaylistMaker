package com.example.playlistmaker.ui.createPlaylist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.use_cases.DataBasePlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class CreatePlaylistViewModel(val dataBasePlaylistInteractor: DataBasePlaylistInteractor) :
    ViewModel() {
    private var createPlaylistState = MutableLiveData<CreatePlaylistState>()

    init {
        createPlaylistState.value = CreatePlaylistState()
    }


    private fun changeState(data: String?, action: Actions) {
        createPlaylistState.value = getCurrentStatus()?.copy(state = AllStates.SAVED_DATA)

        when (action) {
            Actions.SAVE_IMG -> {
                createPlaylistState.value =
                    getCurrentStatus()?.copy(uri = data)
            }

            Actions.SAVE_NAME -> {
                createPlaylistState.value =
                    data?.let { getCurrentStatus()?.copy(playlistName = it) }

            }

            Actions.SAVE_DESCRIPTION -> {
                createPlaylistState.value =
                    getCurrentStatus()?.copy(description = data)
            }

            Actions.SAVE_ALL -> createPlaylistState.value =
                getCurrentStatus()?.copy(state = AllStates.SAVED_PLAYLIST)

        }
    }

    private fun getCurrentStatus() = createPlaylistState.value

    fun getCurrentData() = createPlaylistState

    fun saveImg(imgUri: String?) {
            changeState(imgUri, Actions.SAVE_IMG)
    }

    fun saveName(playlistName: String) {
        changeState(playlistName, Actions.SAVE_NAME)
    }

    fun saveDescription(playlistDescription: String?) {
            changeState(playlistDescription, Actions.SAVE_DESCRIPTION)
    }

    suspend fun savePlaylist() {
        val data = getCurrentStatus()
        val saveData = viewModelScope.async(Dispatchers.IO) {
            if (data != null) {
                dataBasePlaylistInteractor.createPlaylist(
                    playlistName = data.playlistName,
                    playlistDescription = data.description,
                    imgUri = data.uri
                )
            }
        }

        val changeStatus = viewModelScope.async(Dispatchers.Main) {
            delay(SAVE_DELAY_MLS)
            changeState(data = null, action = Actions.SAVE_ALL)
        }
        saveData.await()
        changeStatus.await()
    }

    companion object {
        const val SAVE_DELAY_MLS = 1000L
    }

    enum class Actions {
        SAVE_IMG,
        SAVE_NAME,
        SAVE_DESCRIPTION,
        SAVE_ALL
    }

}



