package com.example.playlistmaker.data.dao

import com.example.playlistmaker.data.models.PlaylistInfoDao
import com.example.playlistmaker.data.models.PlaylistTrackEntity
import com.example.playlistmaker.domain.api.DataBasePlaylistRepository
import com.example.playlistmaker.domain.consumer.DaoConsumer
import com.example.playlistmaker.domain.models.PlaylistInfo
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DataBasePlaylistRepositoryImp(private val appDatabase: AppDatabase) :
    DataBasePlaylistRepository {

    override fun getPlaylist(playlistPK: Int): Flow<List<Track>> = flow {
        val playlistTracks = appDatabase.playlistDAO().getPlaylist(playlistPK = playlistPK)
        val playlist = playlistTracks.map { playlistTrack ->
            appDatabase.trackDAO().getTrack(playlistTrack.trackPK)
        }
        emit(DaoAdapter.trackEntityToTrack(playlist))
    }

    override fun getPlaylistsInfo(): Flow<DaoConsumer<List<PlaylistInfo>>> = flow {
        val playlists = appDatabase.playlistDAO().getPlaylistsInfo()
        val playlistInfo = playlists.map {
            val tracksNumber = it.id?.let { it1 ->
                appDatabase.playlistDAO().countTracksInPlaylist(
                    it1
                )
            }
            it.id?.let { it1 ->
                PlaylistInfoDao(
                    it1,
                    it.name,
                    it.description,
                    tracksNumber ?: 0
                )
            }
        }
        emit(DaoAdapter.playlistsEntityToPlaylistInfo(playlistInfo))
    }

    override suspend fun createPlaylist(playlistName: String, playlistDescription: String) {
        appDatabase.playlistDAO()
            .insertPlaylist(
                DaoAdapter.playListToPlaylistEntity(
                    playlistName = playlistName,
                    playlistDescription = playlistDescription
                )
            )
    }

    override suspend fun addTrackToPlaylist(playlistPK: Int, trackPK: Int) {
        appDatabase.playlistDAO().insertTrackIntoPlaylist(
            PlaylistTrackEntity(
                playlistPK = playlistPK,
                trackPK = trackPK
            )
        )
    }

    override suspend fun deleteTrackFromPlaylist(playlistPK: Int, trackPK: Int) {
        appDatabase.playlistDAO().deleteTrackFromPlaylist(
            playlistTrack = PlaylistTrackEntity(
                playlistPK = playlistPK,
                trackPK = trackPK
            )
        )
    }

    override suspend fun deletePlaylist(playlistPK: Int) {
        appDatabase.playlistDAO().deletePlaylistsTracks(playlistPK = playlistPK)
        appDatabase.playlistDAO().deletePlaylist(playlistPK = playlistPK)
    }

}