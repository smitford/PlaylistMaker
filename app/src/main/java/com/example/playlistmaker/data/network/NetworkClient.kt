package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.models.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
    var lock: Any
}