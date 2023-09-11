package com.example.playlistmaker.data.dao

import com.example.playlistmaker.data.models.TrackEntity
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Date


object DaoAdapter {
    fun trackEntityToTrack(track: List<TrackEntity>): List<Track> {
        val result = track.map {
            Track(
                trackName = it.trackName,
                artistName = it.artistName,
                trackTimeMillis = it.trackTimeMillis,
                artworkUrl100 = it.artworkUrl100,
                trackId = it.id,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                previewUrl = it.previewUrl,
                country = it.country
            )
        }
        return result
    }

    fun trackToTrackEntity(track: Track): TrackEntity {
        return TrackEntity(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            id = track.trackId,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            previewUrl = track.previewUrl,
            country = track.country,
            timeOfAdding = SimpleDateFormat("ddMMyyyyhhmm").format(Date()).toLong()
        )
    }



}