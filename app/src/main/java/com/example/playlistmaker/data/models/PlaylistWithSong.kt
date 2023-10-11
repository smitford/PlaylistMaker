package com.example.playlistmaker.data.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistWithSong(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistPK",
        entityColumn = "trackPK",
        associateBy = Junction(PlaylistTrackEntity::class)
    )
    val playlists: List<TrackEntity>
)