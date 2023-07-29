package com.example.playlistmaker.ui.settings

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding



class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModelFactory(context = applicationContext)
        )[SettingsViewModel::class.java]

        binding.shearButton.setOnClickListener {
            val shearIntent = Intent(Intent.ACTION_SEND)
            shearIntent.data = Uri.parse("")
            shearIntent.type = "text/html"
            shearIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shear_app))
            startActivity(shearIntent)
        }

        binding.supportButton.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SEND)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.student_mail)))
            supportIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.title_of_sup_mail))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.text_of_sup_mail))
            startActivity(supportIntent)

        }

        binding.termsOfUseButton.setOnClickListener {

            val termsOfUseIntent = Intent(Intent.ACTION_WEB_SEARCH)
            termsOfUseIntent.putExtra(SearchManager.QUERY, getString(R.string.term_of_use_web))
            startActivity(termsOfUseIntent)
        }

        binding.backButton.setOnClickListener {
            this.finish()
        }


        binding.themeSwitcher.isChecked =
            settingsViewModel.getSettingsState().value?.themeState ?: false

        settingsViewModel.getSettingsState().observe(this) { settingState ->

            switchTheme(settingState.themeState)
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, _ ->
            settingsViewModel.changeSettingTheme()
        }
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
