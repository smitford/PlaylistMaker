package com.example.playlistmaker.domain.use_cases.implementation

import com.example.playlistmaker.domain.api.DataBaseTrackRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.use_cases.DataBaseTrackInteractor
import kotlinx.coroutines.flow.Flow

class DataBaseTrackInteractorImp(private val repository: DataBaseTrackRepository) : DataBaseTrackInteractor {
    override fun getFavoriteTracks(): Flow<List<Track>> =
        repository.getFavoriteTracks()

    override suspend fun saveFavoriteTrack(track: Track) {
        repository.saveTrackToFav(track = track)
    }

    override fun isTrackFavorite(trackId: Int): Flow<Boolean> =
        repository.isTrackFavorite(trackId = trackId)

    override suspend fun deleteTrackFromFav(trackId: Int) {
        repository.deleteTrackFromFav(trackId = trackId)
    }

    override suspend fun saveTrack(track: Track) {
        repository.saveTrack(track = track)
    }
}