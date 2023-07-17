package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.models.Response
import com.example.playlistmaker.data.models.TrackDto
import com.example.playlistmaker.data.models.TrackSearchRequest
import com.example.playlistmaker.data.models.TrackSearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    override var lock = Any()
    private val iTunesAPI = "https://itunes.apple.com"

    private val retrofitITunes =
        Retrofit.Builder()
            .baseUrl(iTunesAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val iTunesService = retrofitITunes.create(ITunesAPI::class.java)


    override fun doRequest(dto: Any): Response {

        return if (dto is TrackSearchRequest) {

            try {
                val resp = iTunesService.searchTrack(text = dto.term).execute()
                val body = resp.body() ?: Response()
                body.apply { resultCode = resp.code() }
                body
            } catch (e: Exception) {
                Response().apply { resultCode = 400 }
            }

        } else {
            Response().apply { resultCode = 400 }
        }


    }

}