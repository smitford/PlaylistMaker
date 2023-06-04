package com.example.playlistmaker

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.trackRecycleView.Track


class PlayerActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backButton = findViewById<Button>(R.id.back_button_player_act)

        val track =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("track", Track::class.java)
            } else {
                intent.getParcelableExtra("track")
            }

        val albumImageView = findViewById<ImageView>(R.id.album_image)
        val textSongName = findViewById<TextView>(R.id.player_text_song_name)
        val textSongArtist = findViewById<TextView>(R.id.player_text_song_artist)
        val textSongLength = findViewById<TextView>(R.id.text_song_length)
        val textSongAlbum = findViewById<TextView>(R.id.text_song_album)
        val textSongYear = findViewById<TextView>(R.id.text_song_year)
        val textSongGenre = findViewById<TextView>(R.id.text_song_genre)
        val textSongCountry = findViewById<TextView>(R.id.text_song_country)

       val roundedCorners = (8 / (this.resources
            .displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)).toInt()

        Glide.with(albumImageView)
            .load(track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(roundedCorners))
            .into(albumImageView)

        if (track != null) {
            textSongName.text = track.trackName
            textSongArtist.text = track.artistName
            textSongLength.text = track.trackTimeMillis
            textSongAlbum.text = track.collectionName
            textSongYear.text = track.releaseDate.take(4)
            textSongGenre.text = track.primaryGenreName
            textSongCountry.text = track.country
        }




        backButton.setOnClickListener {
            finish()
        }

    }

}