package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.models.TrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {
    @GET("/search?entity=song")
    suspend fun searchTrack(@Query("term") text: String): TrackSearchResponse
}