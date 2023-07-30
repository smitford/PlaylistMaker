package com.example.playlistmaker.domain.use_cases

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.models.Track

interface TrackSearchUseCase {
    fun searchTracks(term: String, consumer: Consumer<List<Track>>)
}