package com.example.playlistmaker.domain.use_cases.implementation

import com.example.playlistmaker.domain.api.TrackSharedPrefRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.use_cases.TrackGetUseCase

class TrackGetUseCaseImp (private val repository: TrackSharedPrefRepository) : TrackGetUseCase {
    override fun execute () : List<Track> = repository.getTrackList()
}