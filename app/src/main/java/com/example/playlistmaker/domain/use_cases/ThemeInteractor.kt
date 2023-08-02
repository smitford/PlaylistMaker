package com.example.playlistmaker.domain.use_cases

interface ThemeInteractor {
    fun saveTheme(isDarkTheme: Boolean)
    fun getTheme(): Boolean
}