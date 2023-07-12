package com.example.playlistmaker.data.shares_pref

import com.example.playlistmaker.data.models.TrackSharedPref
import com.example.playlistmaker.domain.models.Track


object adapterTrackSharedPref {

    fun trackSharedToTrack(track: List<TrackSharedPref>): List<Track> {

        val result = track.map {
            Track(
                trackName = it.trackName,
                artistName = it.artistName,
                trackTimeMillis = it.trackTimeMillis,
                artworkUrl100 = it.artworkUrl100,
                trackId = it.trackId,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                previewUrl = it.previewUrl,
                country = it.country
            )

        }

        return result
    }

    fun trackToTrackShared(track: Track): TrackSharedPref {
        return TrackSharedPref(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            trackId = track.trackId,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            previewUrl = track.previewUrl,
            country = track.country
        )
    }
}