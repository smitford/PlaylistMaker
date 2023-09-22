package com.example.playlistmaker.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.models.PlaylistEntity
import com.example.playlistmaker.data.models.PlaylistTrackEntity
import com.example.playlistmaker.data.models.TrackEntity

@Database(
    version = 1,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDAO(): TrackDAO
    abstract fun playlistDAO(): PlaylistDao
}