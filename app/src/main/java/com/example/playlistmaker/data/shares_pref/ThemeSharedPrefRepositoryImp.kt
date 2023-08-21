package com.example.playlistmaker.data.shares_pref

import android.content.Context
import android.util.Log
import com.example.playlistmaker.domain.api.ThemeSharedPrefRepository
import com.example.playlistmaker.domain.models.Theme
import sharedPreferencesInit

class ThemeSharedPrefRepositoryImp(context: Context) : ThemeSharedPrefRepository {
    private val sharedPreferences = sharedPreferencesInit(context)

    override fun getTheme(): Theme = Theme(sharedPreferences.getBoolean(THEME_SETTINGS, false))


    override fun saveTheme(isDarkTheme: Boolean) {
        sharedPreferences.edit().putBoolean(THEME_SETTINGS, isDarkTheme).apply()
        Log.d("Save", "Save")
    }

    companion object {
        const val THEME_SETTINGS = "theme_settings"
    }
}