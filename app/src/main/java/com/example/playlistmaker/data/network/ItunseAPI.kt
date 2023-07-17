package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.models.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ITunesAPI {

    @GET("/search?entity=song")
    fun searchTrack(@Query("term") text: String): Call<TrackSearchResponse>

}