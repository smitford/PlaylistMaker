package com.example.playlistmaker.domain.use_cases

import com.example.playlistmaker.domain.api.TrackSharedPrefRepository

class TrackClearHistoryUseCase (private val repository: TrackSharedPrefRepository) {
    fun execute() = repository.clearHistory()
}