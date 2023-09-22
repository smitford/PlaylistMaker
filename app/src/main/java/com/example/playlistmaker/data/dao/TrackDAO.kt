package com.example.playlistmaker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.models.TrackEntity


@Dao
interface TrackDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table ORDER BY timeOfAdding DESC")
    fun getAllTracks(): List<TrackEntity>

    @Query("SELECT id FROM track_table WHERE id == :trackID AND isFavorite= 1")
    fun getFavTrackId(trackID: Int): Int?

    @Query("SELECT id FROM track_table WHERE id == :trackID")
    fun checkInBase(trackID: Int): Int?

    @Query("SELECT * FROM track_table WHERE id == :trackID")
    fun getTrack(trackID: Int): TrackEntity

    @Query("DELETE FROM track_table WHERE id== :trackID")
    fun delete(trackID: Int)

    @Query("UPDATE track_table SET isFavorite =:isFavorite WHERE id ==:trackId")
    fun updateFavoriteStatus(trackId: Int, isFavorite: Boolean)

}

