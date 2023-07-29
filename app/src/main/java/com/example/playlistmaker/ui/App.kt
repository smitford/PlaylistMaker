package com.example.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import sharedPreferencesInit


const val SEARCH_HISTORY = "search_history"
const val THEME_SETTINGS = "theme_settings"

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = sharedPreferencesInit(applicationContext)
        switchTheme(sharedPreferences.getBoolean(THEME_SETTINGS, false))
    }

    private fun switchTheme(isDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}