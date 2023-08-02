package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.models.Track

sealed interface SearchActivityState{
    object Start : SearchActivityState
    object Loading : SearchActivityState
    object ConnectionError :SearchActivityState
    object InvalidRequest : SearchActivityState
    data class State(val trackList: List<Track>, val state: String) : SearchActivityState
}