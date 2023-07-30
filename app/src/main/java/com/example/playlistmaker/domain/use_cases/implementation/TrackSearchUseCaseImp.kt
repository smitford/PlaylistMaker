package com.example.playlistmaker.domain.use_cases.implementation

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.api.TrackNetworkRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.use_cases.TrackSearchUseCase
import java.util.concurrent.Executors

class TrackSearchUseCaseImp(private val repository: TrackNetworkRepository): TrackSearchUseCase {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(term: String, consumer: Consumer<List<Track>>) {
        executor.execute {
            consumer.consume(repository.searchTracks(term = term))
        }
    }
}