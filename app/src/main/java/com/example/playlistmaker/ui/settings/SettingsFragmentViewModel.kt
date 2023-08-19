package com.example.playlistmaker.ui.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.use_cases.ThemeInteractor



class SettingsFragmentViewModel(
    private val themeInteractor: ThemeInteractor
) : ViewModel() {

    private var themeState = MutableLiveData<SettingsFragmentState>()

    init {
        getThemeState()
    }

    private fun getThemeState() {
        themeState.value = getCurrentState().copy(themeState = themeInteractor.getTheme())
    }

    private fun saveThemeState() {
        Log.d("Save in ViewModel", "Theme Saved")
        themeInteractor.saveTheme(getCurrentState().themeState)
    }

    private fun getCurrentState(): SettingsFragmentState =
        themeState.value ?: SettingsFragmentState(false)

    fun getSettingsState(): LiveData<SettingsFragmentState> = themeState

    fun changeSettingTheme() {
        Log.d("Change Theme", "Theme Changed")
        themeState.value = SettingsFragmentState(!getCurrentState().themeState)
        saveThemeState()
    }

}