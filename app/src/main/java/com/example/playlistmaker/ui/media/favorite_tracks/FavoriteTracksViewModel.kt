package com.example.playlistmaker.ui.media.favorite_tracks

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.use_cases.DataBaseInteractor
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(val dataBase: DataBaseInteractor) : ViewModel() {

    private var favoriteTracksList = MutableLiveData<FavoriteTracksState>()

    private fun changeState(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            favoriteTracksList.value = FavoriteTracksState.Empty
            Log.d("Check for Empty", "Media empty")
        } else {
            favoriteTracksList.value = FavoriteTracksState.HasTracks(tracks = tracks)
            Log.d("Check for Fav", "Media: $tracks")
        }
    }

    fun updateFavList() {
        viewModelScope.launch {
            dataBase.getFavoriteTracks().collect { tracks ->
                changeState(tracks = tracks)
            }
        }
    }

    fun getStatus(): MutableLiveData<FavoriteTracksState> = favoriteTracksList
}