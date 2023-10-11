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

    @Query("SELECT * FROM track_table WHERE isFavorite == 1 ORDER BY timeOfAdding DESC")
    fun getAllTracks(): List<TrackEntity>

    @Query("SELECT trackPK FROM track_table WHERE trackPK == :trackID AND isFavorite= 1")
    fun getFavTrackId(trackID: Int): Int?

    @Query("SELECT trackPK FROM track_table WHERE trackPK == :trackID")
    fun checkInBase(trackID: Int): Int?

    @Query("SELECT * FROM track_table WHERE trackPK == :trackID")
    fun getTrack(trackID: Int): TrackEntity

    @Query("DELETE FROM track_table WHERE trackPK== :trackId")
    fun delete(trackId: Int)

    @Query("UPDATE track_table SET isFavorite =:isFavorite WHERE trackPK ==:trackId")
    fun updateFavoriteStatus(trackId: Int, isFavorite: Boolean)

}

