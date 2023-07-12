package com.example.playlistmaker.domain.consumer

import com.example.playlistmaker.domain.models.Track

interface Consumer<T> {
    fun consume(data: DataConsumer<T>)
}