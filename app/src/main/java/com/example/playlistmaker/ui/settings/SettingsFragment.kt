package com.example.playlistmaker.ui.settings


import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val settingsFragmentViewModel by viewModel<SettingsFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.themeSwitcher.isChecked =
            settingsFragmentViewModel.getSettingsState().value?.themeState ?: false

        settingsFragmentViewModel.getSettingsState().observe(viewLifecycleOwner) { settingState ->

            switchTheme(settingState.themeState)
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, _ ->
            settingsFragmentViewModel.changeSettingTheme()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
