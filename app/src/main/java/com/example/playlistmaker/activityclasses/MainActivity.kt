package com.example.playlistmaker.activityclasses

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.R

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mediaButton = findViewById<Button>(R.id.media_button)
        val settingButton = findViewById<Button>(R.id.settings_button)
        val searchButton = findViewById<Button>(R.id.search_button)


        mediaButton.setOnClickListener {
            val displayMedia = Intent(this, MediaActivity::class.java)
            startActivity(displayMedia)
        }

        settingButton.setOnClickListener {
            val displaySettings = Intent(this, SettingsActivity::class.java)
            startActivity(displaySettings)
        }
        searchButton.setOnClickListener {
            val displaySearch = Intent(this, SearchActivity::class.java)
            startActivity(displaySearch)
        }
    }
}