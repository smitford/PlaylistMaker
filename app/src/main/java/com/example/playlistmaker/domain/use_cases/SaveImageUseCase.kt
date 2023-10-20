package com.example.playlistmaker.domain.use_cases

import android.content.Context

interface SaveImageUseCase {
    fun execute(uri: String, context: Context, playlistName: String)
}