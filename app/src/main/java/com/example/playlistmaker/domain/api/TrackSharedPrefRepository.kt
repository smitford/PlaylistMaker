package com.example.playlistmaker.domain.api

import android.content.Context
import com.example.playlistmaker.domain.models.Track

interface TrackSharedPrefRepository  {
    fun getTrackList(): List<Track>
    fun saveTrackList(track: Track)
    fun clearHistory()
}