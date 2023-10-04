package com.example.playlistmaker.domain.use_cases.implementation

import com.example.playlistmaker.domain.api.DataBasePlaylistRepository
import com.example.playlistmaker.domain.consumer.DaoConsumer
import com.example.playlistmaker.domain.models.PlaylistInfo
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.use_cases.DataBasePlaylistInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class DataBasePlaylistInteractorImp(private val repository: DataBasePlaylistRepository) :
    DataBasePlaylistInteractor {
    override fun getPlaylist(playlistPK: Int) =
        repository.getPlaylist(playlistPK = playlistPK)

    override fun getPlaylistsInfo(): Flow<List<PlaylistInfo>?> {
        return repository.getPlaylistsInfo().map { result ->
            when (result) {
                is DaoConsumer.Success -> result.data
                is DaoConsumer.Error -> null
            }
        }
    }


    override suspend fun createPlaylist(
        playlistName: String,
        playlistDescription: String?,
        imgUri: String?
    ) =
        repository.createPlaylist(
            playlistName = playlistName,
            playlistDescription = playlistDescription,
            imgUri = imgUri
        )

    override fun addTrackToPlaylist(playlistPK: Int, trackPK: Int) =
        repository.addTrackToPlaylist(playlistPK = playlistPK, trackPK = trackPK)


    override suspend fun deleteTrackFromPlaylist(playlistPK: Int, trackPK: Int) {
        repository.deleteTrackFromPlaylist(
            playlistPK = playlistPK,
            trackPK = trackPK
        )
    }

    override suspend fun deletePlaylist(playlistPK: Int) =
        repository.deletePlaylist(playlistPK = playlistPK)

}