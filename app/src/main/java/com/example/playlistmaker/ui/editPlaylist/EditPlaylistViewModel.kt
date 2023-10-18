package com.example.playlistmaker.ui.editPlaylist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.PlaylistInfo
import com.example.playlistmaker.domain.use_cases.DataBasePlaylistInteractor
import com.example.playlistmaker.ui.createPlaylist.AllStates
import com.example.playlistmaker.ui.createPlaylist.CreatePlaylistViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    playlistId: Int,
    dataBasePlaylistInteractor: DataBasePlaylistInteractor
) :
    CreatePlaylistViewModel(dataBasePlaylistInteractor = dataBasePlaylistInteractor) {
    lateinit var playlistinfo: PlaylistInfo
    private val editPlaylistState = MutableLiveData<PlaylistInfo>()


    init {
        viewModelScope.launch(Dispatchers.Main) {
            val result = async {
                dataBasePlaylistInteractor.getPlaylistInfo(playlistPK = playlistId).collect {
                    playlistinfo = it
                }
            }

            result.await()
            Log.d("data", "${playlistinfo.imgUri}")
            editPlaylistState.value = playlistinfo
        }

    }

    fun getEditPlaylistState() = editPlaylistState

    override suspend fun savePlaylist() {
        val data = getCurrentData().value

        val saveData = viewModelScope.async(Dispatchers.IO) {
            if (data != null)
                dataBasePlaylistInteractor.updatePlaylistInfo(
                    PlaylistInfo(
                        id = playlistinfo.id,
                        name = data.playlistName,
                        description = data.description,
                        imgUri = data.uri,
                        tracksNumber = playlistinfo.tracksNumber
                    )
                )
        }

        val changeStatus = viewModelScope.async(Dispatchers.Main) {
            delay(SAVE_DELAY_MLS)
            getCurrentStatus()?.copy(state = AllStates.SAVED_PLAYLIST)
        }
        saveData.await()
        changeStatus.await()
    }

}