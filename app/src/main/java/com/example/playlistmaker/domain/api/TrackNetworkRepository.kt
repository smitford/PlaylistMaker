package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.consumer.DataConsumer
import com.example.playlistmaker.domain.models.Track

interface TrackNetworkRepository {
    fun searchTracks(term: String): DataConsumer<List<Track>>
}