package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface DataBaseRepository {
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun saveTrackToFav(track: Track)
    fun isTrackFavorite(trackID: Int): Flow<Boolean>
    suspend fun deleteTrack(track: Track)
}