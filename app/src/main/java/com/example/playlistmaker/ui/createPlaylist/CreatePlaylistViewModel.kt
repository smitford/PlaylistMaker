package com.example.playlistmaker.ui.createPlaylist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.use_cases.DataBasePlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log

class CreatePlaylistViewModel(val dataBasePlaylistInteractor: DataBasePlaylistInteractor) :
    ViewModel() {
    private var createPlaylistState = MutableLiveData<CreatePlaylistState>()

    init {
        createPlaylistState.value = CreatePlaylistState()
    }


    private fun changeState(data: String?, newState: AllStates) {
        when (newState) {
            AllStates.SAVED_IMG -> {
                createPlaylistState.value =
                    getCurrentStatus()?.apply { this.uri = data; this.state = AllStates.SAVED_IMG }
                Log.d("Sae Description", "${createPlaylistState.value?.state}")
            }

            AllStates.SAVED_NAME -> {
                createPlaylistState.value =
                    getCurrentStatus()?.apply {
                        this.playlistName = data!!; this.state = AllStates.SAVED_NAME
                    }
                Log.d("Sae Description", "${createPlaylistState.value?.state}")
            }

            AllStates.SAVED_DESCRIPTION -> {
                createPlaylistState.value =
                    getCurrentStatus()?.apply {
                        this.description = data!!; this.state = AllStates.SAVED_DESCRIPTION
                    }
                Log.d("Sae Description", "${createPlaylistState.value?.state}")
            }

            AllStates.SAVED_PLAYLIST -> createPlaylistState.value =
                getCurrentStatus()?.copy(state = AllStates.SAVED_PLAYLIST)

            else -> createPlaylistState.value = getCurrentStatus()
        }
    }

    private fun getCurrentStatus() = createPlaylistState.value

    fun getCurrentData() = createPlaylistState

    fun saveImg(imgUri: String?) {
        if (imgUri != null)
            changeState(imgUri, AllStates.SAVED_IMG)
    }

    fun saveName(playlistName: String) {
        changeState(playlistName, AllStates.SAVED_NAME)
    }

    fun saveDescription(playlistDescription: String?) {
        if (playlistDescription != null)
            changeState(playlistDescription, AllStates.SAVED_DESCRIPTION)
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
            changeState(data = null, newState = AllStates.SAVED_PLAYLIST)
        }
        saveData.await()
        changeStatus.await()
    }

    companion object{
        const val SAVE_DELAY_MLS=1000L
    }

}



