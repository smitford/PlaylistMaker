package com.example.playlistmaker.data.shares_pref

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.domain.api.ThemeSharedPrefRepository
import sharedPreferencesInit

class ThemeSharedPrefRepositoryImp(context: Context) : ThemeSharedPrefRepository {
    companion object {
        const val THEME_SETTINGS = "theme_settings"
    }
    private val sharedPreferences = sharedPreferencesInit(context)

    override fun getTheme(): Boolean = sharedPreferences.getBoolean(THEME_SETTINGS, false)


    override fun saveTheme(isDarkTheme: Boolean) {
        sharedPreferences.edit().putBoolean(THEME_SETTINGS, isDarkTheme).apply()
        Log.d("Save","Save")
    }
}