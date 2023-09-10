package com.example.playlistmaker.ui.media.favorite_tracks

import com.example.playlistmaker.domain.models.Track

sealed interface FavoriteTracksState {
    object Empty : FavoriteTracksState
    data class HasTracks(val tracks: List<Track>) : FavoriteTracksState
}