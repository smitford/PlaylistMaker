package com.example.playlistmaker.domain.consumer

sealed interface DaoConsumer<T> {
    data class Error<T>(val errorMassage: String) : DaoConsumer<T>
    data class Success<T>(val data: T) : DaoConsumer<T>
}