package com.example.playlistmaker.domain.use_cases

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface DataBaseInteractor {
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun saveFavoriteTrack(track: Track)
    fun isTrackFavorite(trackID: Int): Flow<Boolean>
    suspend fun deleteTrack(track: Track)
}