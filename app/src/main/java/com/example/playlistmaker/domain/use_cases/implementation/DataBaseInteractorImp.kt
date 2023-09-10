package com.example.playlistmaker.domain.use_cases.implementation

import com.example.playlistmaker.domain.api.DataBaseRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.use_cases.DataBaseInteractor
import kotlinx.coroutines.flow.Flow

class DataBaseInteractorImp(private val repository: DataBaseRepository) : DataBaseInteractor {
    override fun getFavoriteTracks(): Flow<List<Track>> =
        repository.getFavoriteTracks()

    override suspend fun saveFavoriteTrack(track: Track) {
        repository.saveTrackToFav(track = track)
    }

    override fun isTrackFavorite(trackID: Int): Flow<Boolean> =
        repository.isTrackFavorite(trackID = trackID)

    override suspend fun deleteTrack(track: Track) {
        repository.deleteTrack(track = track)
    }
}