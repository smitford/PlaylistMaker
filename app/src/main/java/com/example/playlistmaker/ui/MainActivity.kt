package com.example.playlistmaker.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)



        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.termOfUseFragment, R.id.playerFragment, R.id.createPlaylistFragment -> {
                    binding.bottomNavigation.visibility = View.GONE
                    binding.divider.visibility = View.GONE
                }

                else -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    binding.divider.visibility = View.VISIBLE
                }
            }
        }
    }
}