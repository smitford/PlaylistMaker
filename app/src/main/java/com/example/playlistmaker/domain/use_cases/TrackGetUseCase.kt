package com.example.playlistmaker.domain.use_cases

import com.example.playlistmaker.domain.models.Track

interface TrackGetUseCase {
    fun execute () : List<Track>
}