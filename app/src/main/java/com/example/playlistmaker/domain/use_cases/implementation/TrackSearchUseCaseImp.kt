package com.example.playlistmaker.domain.use_cases.implementation

import com.example.playlistmaker.domain.api.TrackNetworkRepository
import com.example.playlistmaker.domain.consumer.DataConsumer
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.use_cases.TrackSearchUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class TrackSearchUseCaseImp(private val repository: TrackNetworkRepository) : TrackSearchUseCase {

    override fun searchTracks(term: String): Flow<List<Track>?> {
        return repository.searchTracks(term = term).map { result ->
            when (result) {
                is DataConsumer.Success -> result.data
                is DataConsumer.Error -> null
            }
        }
    }
}