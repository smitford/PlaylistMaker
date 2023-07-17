package com.example.playlistmaker.domain.use_cases

import com.example.playlistmaker.domain.api.TrackSharedPrefRepository
import com.example.playlistmaker.domain.models.Track

class TrackGetUseCase (private val repository: TrackSharedPrefRepository) {
    fun execute () : List<Track> = repository.getTrackList()
}