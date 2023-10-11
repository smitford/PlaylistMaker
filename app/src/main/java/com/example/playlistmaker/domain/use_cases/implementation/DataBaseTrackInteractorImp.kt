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

    override fun isTrackFavorite(trackID: Int): Flow<Boolean> =
        repository.isTrackFavorite(trackID = trackID)

    override suspend fun deleteTrackFromFav(trackID: Int) {
        repository.deleteTrackFromFav(trackID = trackID)
    }

    override suspend fun saveTrack(track: Track) {
        repository.saveTrack(track = track)
    }
}