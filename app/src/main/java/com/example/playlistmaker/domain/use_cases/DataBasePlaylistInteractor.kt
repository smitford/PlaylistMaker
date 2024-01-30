package com.example.playlistmaker.domain.use_cases

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.PlaylistInfo
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface DataBasePlaylistInteractor {
    fun getPlaylist(playlistPK: Int): Flow<Playlist>

    fun getPlaylistInfo(playlistPK: Int): Flow<PlaylistInfo>

    fun getPlaylistsInfo(): Flow<List<PlaylistInfo>?>

    suspend fun createPlaylist(playlistName: String, playlistDescription: String?, imgUri: String?)

    fun addTrackToPlaylist(playlistPK: Int, trackPK: Int): Flow<Boolean>

    suspend fun deleteTrackFromPlaylist(playlistPK: Int, trackPK: Int)

    suspend fun deletePlaylist(playlistPK: Int)

    suspend fun updatePlaylistInfo(playlistInfo: PlaylistInfo)
}