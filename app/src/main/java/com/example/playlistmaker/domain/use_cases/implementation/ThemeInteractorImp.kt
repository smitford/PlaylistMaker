package com.example.playlistmaker.domain.use_cases.implementation

import com.example.playlistmaker.domain.api.ThemeSharedPrefRepository
import com.example.playlistmaker.domain.use_cases.ThemeInteractor

class ThemeInteractorImp(private val repository: ThemeSharedPrefRepository) : ThemeInteractor {
    override fun saveTheme(isDarkTheme: Boolean) {
        repository.saveTheme(isDarkTheme = isDarkTheme)
    }

    override fun getTheme(): Boolean = repository.getTheme().isDark
}