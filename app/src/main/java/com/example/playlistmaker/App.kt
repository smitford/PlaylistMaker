package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.trackRecycleView.Track
import sharedPreferencesInit

const val THEME_PREFERENCES_AND_HISTORY = "theme_preferences"
const val THEME_SETTINGS = "theme_settings"
const val SEARCH_HISTORY = "search_history"

var darkTheme = false


class App : Application() {

    lateinit var sharedPreferencesThemeAndHistory: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sharedPreferencesThemeAndHistory = sharedPreferencesInit(this.applicationContext)

        darkTheme = sharedPreferencesThemeAndHistory.getBoolean(THEME_SETTINGS, false)
        switchTheme(darkTheme)

    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPreferencesThemeAndHistory.edit().putBoolean(THEME_SETTINGS, darkTheme).apply()

    }
}