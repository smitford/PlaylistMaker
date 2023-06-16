package com.example.playlistmaker

import com.example.playlistmaker.api.ITunesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {
    @GET("/search?entity=song")
    fun searchTrack(@Query("term") text: String): Call<ITunesResponse>
}