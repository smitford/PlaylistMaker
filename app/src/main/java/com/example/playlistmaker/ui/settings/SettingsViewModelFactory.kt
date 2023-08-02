package com.example.playlistmaker.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator

class SettingsViewModelFactory(val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return SettingsViewModel(
            Creator.getSaveThemeUseCase(context = context)
        ) as T
    }
}