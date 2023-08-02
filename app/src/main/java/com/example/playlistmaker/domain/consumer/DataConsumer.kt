package com.example.playlistmaker.domain.consumer


sealed interface DataConsumer<T> {
    data class Error<T>(val errorCode: Int) : DataConsumer<T>
    data class Success<T>(val data: T) : DataConsumer<T>
}