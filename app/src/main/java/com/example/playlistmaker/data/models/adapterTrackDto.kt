package com.example.playlistmaker.data.models

import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*

object adapterTrackDto {
    fun trackDtoToTrack(track: List<TrackDto>): List<Track> {

        val result = track.map {
            Track(
                trackName = it.trackName,
                artistName = it.artistName,
                trackTimeMillis = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(it.trackTimeMillis.toLong()),
                artworkUrl100 = it.artworkUrl100,
                trackId = it.trackId,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate.take(4) ,
                primaryGenreName = it.primaryGenreName,
                previewUrl = it.previewUrl,
                country = it.country
            )

        }

        return result
    }
}