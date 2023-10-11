package com.example.playlistmaker.data.dao

import com.example.playlistmaker.data.models.PlaylistEntity
import com.example.playlistmaker.data.models.PlaylistInfoDao
import com.example.playlistmaker.data.models.TrackEntity
import com.example.playlistmaker.domain.consumer.DaoConsumer
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.PlaylistInfo
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Date


object DaoAdapter {
    fun trackEntityToTrack(trackList: List<TrackEntity>): List<Track> = trackList.map {
        Track(
            trackName = it.trackName,
            artistName = it.artistName,
            trackTimeMillis = it.trackTimeMillis,
            artworkUrl100 = it.artworkUrl100,
            trackId = it.trackPK,
            collectionName = it.collectionName,
            releaseDate = it.releaseDate,
            primaryGenreName = it.primaryGenreName,
            previewUrl = it.previewUrl,
            country = it.country
        )
    }

    fun trackToTrackEntity(track: Track, isFavorite: Boolean): TrackEntity =
        TrackEntity(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            trackPK = track.trackId,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            previewUrl = track.previewUrl,
            country = track.country,
            timeOfAdding = SimpleDateFormat("ddMMyyyyhhmm").format(Date()).toLong(),
            isFavorite = isFavorite
        )

    fun playListToPlaylistEntity(
        playlistName: String,
        playlistDescription: String?,
        imgUri: String?
    ) = PlaylistEntity(
        playlistPK = null,
        name = playlistName,
        description = playlistDescription,
        imgUri = imgUri
    )

    fun playlistEntityToPlaylist(playlist: PlaylistEntity, playlistTracks: List<Track>) =
        playlist.playlistPK?.let {
            Playlist(
                id = it,
                name = playlist.name,
                description = playlist.description,
                imgUri = playlist.imgUri,
                tracks = playlistTracks
            )
        }

    fun playlistsEntityToPlaylistInfo(playlist: List<PlaylistInfoDao?>): DaoConsumer<List<PlaylistInfo>> {
        val result = DaoConsumer.Success(playlist.map { playlistComp ->
            if (playlistComp != null)
                PlaylistInfo(
                    id = playlistComp.id,
                    name = playlistComp.name,
                    description = playlistComp.description,
                    imgUri = playlistComp.imgUri,
                    tracksNumber = playlistComp.tracksNumber
                )
            else
                return DaoConsumer.Error("NullPointException")
        })

        return result
    }

}