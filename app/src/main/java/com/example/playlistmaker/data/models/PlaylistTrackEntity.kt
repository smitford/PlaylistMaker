package com.example.playlistmaker.data.models

import androidx.room.Entity

@Entity(tableName = "playlist_tracks", primaryKeys = ["playlistPK", "trackPK"])
data class PlaylistTrackEntity(val playlistPK: Int, val trackPK: Int)
