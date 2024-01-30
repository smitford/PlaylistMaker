package com.example.playlistmaker.data.dao

import android.util.Log
import com.example.playlistmaker.data.models.PlaylistInfoDao
import com.example.playlistmaker.data.models.PlaylistTrackEntity
import com.example.playlistmaker.domain.api.DataBasePlaylistRepository
import com.example.playlistmaker.domain.consumer.DaoConsumer
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.PlaylistInfo
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DataBasePlaylistRepositoryImp(private val appDatabase: AppDatabase) :
    DataBasePlaylistRepository {

    override fun getPlaylist(playlistId: Int): Flow<Playlist> = flow {
        val playlistsWithTracks = appDatabase.playlistDAO().getPlaylistWithTracks()
        playlistsWithTracks.map {
            if (it.playlist.playlistPK == playlistId) emit(
                DaoAdapter.playlistEntityToPlaylist(
                    it
                )
            )
        }

    }.flowOn(Dispatchers.IO)

    override fun getPlaylistInfo(playlistPK: Int): Flow<PlaylistInfo> =
        flow {
            val playlistEntity = appDatabase.playlistDAO().getPlaylistInfo(playlistPK = playlistPK)
            val trackNumber =
                appDatabase.playlistDAO().countTracksInPlaylist(playlistId = playlistPK)
            emit(
                DaoAdapter.playlistEntityToPlaylistInfo(
                    playlistEntity = playlistEntity,
                    trackNumber
                )
            )
        }.flowOn(Dispatchers.IO)

    override fun getPlaylistsInfo(): Flow<DaoConsumer<List<PlaylistInfo>>> = flow {
        val playlists = appDatabase.playlistDAO().getPlaylistsInfo()
        val playlistInfo = playlists.map {
            val tracksNumber = it.playlistPK?.let { it1 ->
                appDatabase.playlistDAO().countTracksInPlaylist(
                    it1
                )
            }
            it.playlistPK?.let { it1 ->
                PlaylistInfoDao(
                    it1,
                    it.name,
                    it.description,
                    it.imgUri,
                    tracksNumber ?: 0
                )
            }
        }
        emit(DaoAdapter.playlistsEntityToPlaylistInfo(playlistInfo))
    }.flowOn(Dispatchers.IO)

    override suspend fun createPlaylist(
        playlistName: String,
        playlistDescription: String?,
        imgUri: String?
    ) {
        appDatabase.playlistDAO()
            .insertPlaylist(
                DaoAdapter.playListToPlaylistEntity(
                    playlistName = playlistName,
                    playlistDescription = playlistDescription,
                    imgUri = imgUri
                )
            )
    }

    override fun addTrackToPlaylist(playlistId: Int, trackId: Int): Flow<Boolean> = flow {
        val result = appDatabase.playlistDAO()
            .isPlaylistIncludeTrack(playlistId = playlistId, trackId = trackId)
        Log.d("Result of tr search", "$result")
        if (result == null) {
            appDatabase.playlistDAO().insertTrackIntoPlaylist(
                PlaylistTrackEntity(
                    playlistPK = playlistId,
                    trackPK = trackId
                )
            )
            emit(true)
        } else
            emit(false)
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int) {
        appDatabase.playlistDAO().deleteTrackFromPlaylist(
            playlistTrack = PlaylistTrackEntity(
                playlistPK = playlistId,
                trackPK = trackId
            )
        )
        if (isFavorite(trackId = trackId) == null && checkPresenceInPlaylists(trackId = trackId) == 0)
            delete(trackId = trackId)

    }

    override suspend fun deletePlaylist(playlistId: Int) {
        appDatabase.playlistDAO().deletePlaylistsTracks(playlistPK = playlistId)
        appDatabase.playlistDAO().deletePlaylist(playlistPK = playlistId)
    }

    override suspend fun updatePlaylistInfo(playlistInfo: PlaylistInfo) {
        appDatabase.playlistDAO()
            .updatePlaylist(playlistEntity = DaoAdapter.playlistInfoToPlaylistEntity(playlistInfo = playlistInfo))
    }

    private fun delete(trackId: Int) {
        appDatabase.trackDAO().delete(trackId = trackId)
    }

    private fun isFavorite(trackId: Int) =
        appDatabase.trackDAO().getFavTrackId(trackID = trackId)

    private fun checkPresenceInPlaylists(trackId: Int): Int =
        appDatabase.playlistDAO().isTrackPresenceInPlaylists(trackPK = trackId)

}