package com.example.playlistmaker.domain.use_cases

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.api.TrackNetworkRepository
import com.example.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

class TrackSearchUseCase(private val repository: TrackNetworkRepository) {

    private val executor = Executors.newCachedThreadPool()

    fun searchTracks(term: String, consumer: Consumer<List<Track>>) {
        executor.execute {
            consumer.consume(repository.searchTracks(term = term))
        }
    }
}