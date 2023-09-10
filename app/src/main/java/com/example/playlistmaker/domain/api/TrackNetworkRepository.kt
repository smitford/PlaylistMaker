package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.consumer.DataConsumer
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackNetworkRepository {
    fun searchTracks(term: String): Flow<DataConsumer<List<Track>>>
}