package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.models.Track

sealed interface SearchFragmentState{
    object Start : SearchFragmentState
    object Loading : SearchFragmentState
    object ConnectionError :SearchFragmentState
    object InvalidRequest : SearchFragmentState
    data class State(val trackList: List<Track>, val state: String) : SearchFragmentState
}