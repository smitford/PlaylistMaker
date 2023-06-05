package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.trackRecycleView.Track
import handler
import java.text.SimpleDateFormat
import java.util.*


class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val PLAY_DEBOUNCE_DELAY = 250L
    }



    private var playerState = STATE_DEFAULT

    private var mediaPlayer = MediaPlayer()

    lateinit var playButton: ImageButton

    lateinit var songPlayTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playButton = findViewById(R.id.player_button_play)
        songPlayTime = findViewById(R.id.song_play_time)

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
        var url = track?.previewUrl
        prepareMediaPlayer(url.toString())

        playButton.setOnClickListener {
            playbackControl()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        pauseMediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun prepareMediaPlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.play)
            songPlayTime.setText(R.string.play_time)
            playerState = STATE_PREPARED
        }
    }

    private fun startMediaPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        playButton.setImageResource(R.drawable.pause)
        handler.post(playerTimeRefresher())
    }

    private fun pauseMediaPlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        playButton.setImageResource(R.drawable.play)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> pauseMediaPlayer()
            STATE_PREPARED, STATE_PAUSED -> startMediaPlayer()
        }
    }

    private fun playerTimeRefresher(): Runnable {

        return object : Runnable {
            override fun run() {

                if (playerState == STATE_PLAYING) {
                    songPlayTime.text =
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(mediaPlayer.currentPosition)
                    handler.postDelayed(this, PLAY_DEBOUNCE_DELAY)
                }

            }
        }

    }

}