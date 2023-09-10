package com.example.playlistmaker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.models.TrackEntity


@Dao
interface TrackDAO {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table " )
    fun getAllTracks(): List<TrackEntity>

    @Query("SELECT id FROM track_table WHERE id == :trackID")
    fun getTrack(trackID: Int): Int?

    @Delete(entity = TrackEntity::class)
    fun delete(track: TrackEntity)
}