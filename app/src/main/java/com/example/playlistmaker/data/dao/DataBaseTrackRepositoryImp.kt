package com.example.playlistmaker.data.dao

import com.example.playlistmaker.domain.api.DataBaseTrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class DataBaseTrackRepositoryImp(private val appDatabase: AppDatabase) : DataBaseTrackRepository {
    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDAO().getAllTracks()
        emit(DaoAdapter.trackEntityToTrack(trackList = tracks))
    }.flowOn(Dispatchers.IO)

    override suspend fun saveTrackToFav(track: Track) {
        val checkForPresence = appDatabase.trackDAO().checkInBase(trackID = track.trackId)
        if (checkForPresence == null) {
            val trackEnt = DaoAdapter.trackToTrackEntity(track = track, isFavorite = true)
            appDatabase.trackDAO().insertTrack(trackEnt)
        } else
            appDatabase.trackDAO().updateFavoriteStatus(trackId = track.trackId, true)
    }

    override fun isTrackFavorite(trackID: Int): Flow<Boolean> = flow {
        val track = appDatabase.trackDAO().getFavTrackId(trackID = trackID)
        emit(track != null)
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteTrackFromFav(trackID: Int) {
        appDatabase.trackDAO().updateFavoriteStatus(trackId = trackID, isFavorite = false)
    }
}