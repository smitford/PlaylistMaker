package com.example.playlistmaker.domain.api

interface ThemeSharedPrefRepository {
    fun saveTheme(isDarkTheme: Boolean)
    fun getTheme(): Boolean
}