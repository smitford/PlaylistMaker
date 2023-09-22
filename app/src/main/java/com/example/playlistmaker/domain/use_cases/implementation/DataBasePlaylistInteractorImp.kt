package com.example.playlistmaker.domain.use_cases.implementation

import com.example.playlistmaker.domain.api.DataBasePlaylistRepository
import com.example.playlistmaker.domain.consumer.DaoConsumer
import com.example.playlistmaker.domain.models.PlaylistInfo
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.use_cases.DataBasePlaylistInteractor
import kotlinx.coroutines.flow.Flow

class DataBasePlaylistInteractorImp(private val repository: DataBasePlaylistRepository) :
    DataBasePlaylistInteractor {
    override fun getPlaylist(playlistPK: Int): Flow<List<Track>> =
        repository.getPlaylist(playlistPK = playlistPK)

    override fun getPlaylistsInfo(): Flow<DaoConsumer<List<PlaylistInfo>>> =
        repository.getPlaylistsInfo()

    override suspend fun createPlaylist(playlistName: String, playlistDescription: String) =
        repository.createPlaylist(
            playlistName = playlistName,
            playlistDescription = playlistDescription
        )

    override suspend fun addTrackToPlaylist(playlistPK: Int, trackPK: Int) {
        repository.addTrackToPlaylist(playlistPK = playlistPK, trackPK = trackPK)
    }

    override suspend fun deleteTrackFromPlaylist(playlistPK: Int, trackPK: Int) {
        repository.deleteTrackFromPlaylist(
            playlistPK = playlistPK,
            trackPK = trackPK
        )
    }

    override suspend fun deletePlaylist(playlistPK: Int) {
        repository.deletePlaylist(playlistPK = playlistPK)
    }
}