package com.example.playlistmaker.data.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.data.models.Response
import com.example.playlistmaker.data.models.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(val context: Context) : NetworkClient {

    override var lock = Any()
    private val iTunesAPI = "https://itunes.apple.com"

    private val retrofitITunes =
        Retrofit.Builder()
            .baseUrl(iTunesAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val iTunesService = retrofitITunes.create(ITunesAPI::class.java)


    override fun doRequest(dto: Any): Response {

        if (!isConnected()) return Response().apply { resultCode = -1 }

        return if (dto is TrackSearchRequest) {

            try {
                val resp = iTunesService.searchTrack(text = dto.term).execute()
                val body = resp.body() ?: Response()
                body.apply { resultCode = resp.code() }
                body
            } catch (e: Exception) {
                Response().apply { resultCode = -1 }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }


    @SuppressLint("NewApi")
    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}