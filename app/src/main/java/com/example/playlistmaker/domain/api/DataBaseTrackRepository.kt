package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface DataBaseTrackRepository {
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun saveTrackToFav(track: Track)
    fun isTrackFavorite(trackId: Int): Flow<Boolean>
    suspend fun deleteTrackFromFav(trackId: Int)
    suspend fun saveTrack(track: Track)

}