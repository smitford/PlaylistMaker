package com.example.playlistmaker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "playlist")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    val description: String?,
    val imgUri: String?
)
