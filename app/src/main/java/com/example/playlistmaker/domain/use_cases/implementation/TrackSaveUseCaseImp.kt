package com.example.playlistmaker.domain.use_cases.implementation

import com.example.playlistmaker.domain.api.TrackSharedPrefRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.use_cases.TrackSaveUseCase

class TrackSaveUseCaseImp (private val repository: TrackSharedPrefRepository) : TrackSaveUseCase {
    override fun execute(track: Track) {
        repository.saveTrackList(track)
    }
}