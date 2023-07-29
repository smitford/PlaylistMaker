package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Theme

interface ThemeSharedPrefRepository {
    fun saveTheme(isDarkTheme: Boolean)
    fun getTheme(): Theme
}