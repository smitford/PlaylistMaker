package com.example.playlistmaker.domain.api

import android.content.Context

interface InternalDataChangerRepository {
    fun deleteImg(uri: String)
    fun saveImg(uri: String, context: Context, playlistName: String)
}