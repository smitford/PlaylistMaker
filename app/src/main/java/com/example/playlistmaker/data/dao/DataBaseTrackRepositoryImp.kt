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
        val checkForPresence = checkForPresence(trackId = track.trackId)
        if (checkForPresence == null) {
            val trackEnt = DaoAdapter.trackToTrackEntity(track = track, isFavorite = true)
            appDatabase.trackDAO().insertTrack(trackEnt)
        } else
            appDatabase.trackDAO().updateFavoriteStatus(trackId = track.trackId, true)
    }

    override fun isTrackFavorite(trackId: Int): Flow<Boolean> = flow {
        val track = appDatabase.trackDAO().getFavTrackId(trackID = trackId)
        emit(track != null)
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteTrackFromFav(trackId: Int) {
        appDatabase.trackDAO().updateFavoriteStatus(trackId = trackId, isFavorite = false)
        if (checkPresenceInPlaylists(trackId = trackId) == 0) delete(trackId = trackId)
    }

    override suspend fun saveTrack(track: Track) {
        val checkForPresence = checkForPresence(trackId = track.trackId)
        if (checkForPresence == null) {
            val trackEnt = DaoAdapter.trackToTrackEntity(track = track, isFavorite = false)
            appDatabase.trackDAO().insertTrack(trackEnt)
        }
    }

    private fun delete(trackId: Int) {
        appDatabase.trackDAO().delete(trackId = trackId)
    }

    private fun checkForPresence(trackId: Int): Int? =
        appDatabase.trackDAO().checkInBase(trackID = trackId)

    private fun checkPresenceInPlaylists(trackId: Int): Int =
        appDatabase.playlistDAO().isTrackPresenceInPlaylists(trackPK = trackId)

}