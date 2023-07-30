package com.example.playlistmaker.domain.use_cases.implementation

import com.example.playlistmaker.domain.api.TrackSharedPrefRepository
import com.example.playlistmaker.domain.use_cases.TrackClearHistoryUseCase

class TrackClearHistoryUseCaseImp(private val repository: TrackSharedPrefRepository) :
    TrackClearHistoryUseCase {
    override fun execute() = repository.clearHistory()
}