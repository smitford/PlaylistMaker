package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.models.TrackDto
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*

object AdapterTrackDto {
    fun trackDtoToTrack(track: List<TrackDto>): List<Track> {

        val result = track.map {
            Track(
                trackName = it.trackName,
                artistName = it.artistName,
                trackTimeMillis = it.trackTimeMillis ?:"-",
                artworkUrl100 = it.artworkUrl100,
                trackId = it.trackId,
                collectionName = it.collectionName,
                releaseDate = if (it.releaseDate== null){"-"} else it.releaseDate.take(4),
                primaryGenreName = it.primaryGenreName ?: "-",
                previewUrl = it.previewUrl ?: "-",
                country = it.country
            )

        }
        return result
    }
}