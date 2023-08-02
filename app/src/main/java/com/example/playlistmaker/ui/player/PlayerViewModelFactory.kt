package com.example.playlistmaker.ui.player


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.models.Track

class PlayerViewModelFactory(private val track: Track?) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        PlayerViewModel(
           playerInteractor= Creator.getPlayerInteractor(),
            track = track
        )
                as T
}