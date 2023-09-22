package com.example.playlistmaker.data.dao

import com.example.playlistmaker.domain.api.DataBaseRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class DataBaseRepositoryImp(private val appDatabase: AppDatabase) : DataBaseRepository {
    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDAO().getAllTracks()
        emit(DaoAdapter.trackEntityToTrack(trackList = tracks))
    }.flowOn(Dispatchers.IO)

    override suspend fun saveTrackToFav(track: Track) {
        val trackEnt = DaoAdapter.trackToTrackEntity(track = track)
        appDatabase.trackDAO().insertTrack(trackEnt)
    }

    override fun isTrackFavorite(trackID: Int): Flow<Boolean> = flow {
        val track = appDatabase.trackDAO().getTrack(trackID = trackID)
        emit(track != null)
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteTrack(trackID: Int) {
        appDatabase.trackDAO().delete(trackID= trackID)
    }


}