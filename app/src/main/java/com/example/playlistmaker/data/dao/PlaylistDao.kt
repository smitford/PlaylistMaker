package com.example.playlistmaker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.data.models.PlaylistEntity
import com.example.playlistmaker.data.models.PlaylistTrackEntity
import com.example.playlistmaker.data.models.PlaylistWithSong


@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrackIntoPlaylist(playlistTrack: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist")
    fun getPlaylistsInfo(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_tracks WHERE playlistPK==:playlistPK")
    fun getPlaylist(playlistPK: Int): List<PlaylistTrackEntity>

    @Delete(entity = PlaylistTrackEntity::class)
    fun deleteTrackFromPlaylist(playlistTrack: PlaylistTrackEntity)

    @Query("DELETE FROM playlist WHERE id ==:playlistPK")
    fun deletePlaylist(playlistPK: Int)

    @Query("DELETE FROM playlist_tracks WHERE playlistPK ==:playlistPK")
    fun deletePlaylistsTracks(playlistPK: Int)

    @Query("SELECT COUNT(playlistPK) FROM playlist_tracks WHERE playlistPK ==:playlistPK")
    fun countTracksInPlaylist(playlistPK: Int): Int

    @Query("SELECT * FROM playlist_tracks WHERE playlistPK =:playlistPK AND trackPK =:trackPK")
    fun isPlaylistIncludeTrack(playlistPK: Int, trackPK: Int): PlaylistTrackEntity?

    @Transaction
    @Query("SELECT* FROM playlist")
    fun getPlaylistWithTracks(): List<PlaylistWithSong>
}