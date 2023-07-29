package com.example.playlistmaker.domain.use_cases

import com.example.playlistmaker.domain.api.ThemeSharedPrefRepository

class ThemeInteractor(private val repository: ThemeSharedPrefRepository) {
    fun saveTheme(isDarkTheme: Boolean) {
        repository.saveTheme(isDarkTheme = isDarkTheme)
    }

    fun getTheme(): Boolean = repository.getTheme()
}