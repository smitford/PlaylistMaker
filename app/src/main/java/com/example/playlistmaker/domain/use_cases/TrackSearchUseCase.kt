package com.example.playlistmaker.domain.use_cases


import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackSearchUseCase {
    fun searchTracks(term: String): Flow<List<Track>?>
}