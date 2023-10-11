package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface DataBaseTrackRepository {
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun saveTrackToFav(track: Track)
    fun isTrackFavorite(trackID: Int): Flow<Boolean>
    suspend fun deleteTrackFromFav(trackID: Int)

    suspend fun saveTrack(track: Track)
}