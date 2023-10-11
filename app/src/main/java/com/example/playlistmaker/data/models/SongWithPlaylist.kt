package com.example.playlistmaker.data.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SongWithPlaylist(
    @Embedded val track: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "trackId",
        associateBy = Junction(PlaylistTrackEntity::class)
    )
    val playlists: List<TrackEntity>
)