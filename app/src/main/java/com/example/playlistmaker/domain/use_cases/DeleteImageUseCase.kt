package com.example.playlistmaker.domain.use_cases

import android.content.Context

interface DeleteImageUseCase {
    fun execute(uri: String)
}