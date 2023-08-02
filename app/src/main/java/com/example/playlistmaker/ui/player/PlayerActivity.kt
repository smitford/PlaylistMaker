package com.example.playlistmaker.ui.player

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.models.Track


class PlayerActivity : AppCompatActivity() {
    companion object {
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val track =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("track", Track::class.java)
            } else {
                intent.getParcelableExtra("track")
            }

        playerViewModel = ViewModelProvider(
            this,
            PlayerViewModelFactory(track = track)
        )[PlayerViewModel::class.java]

        val roundedCorners = (8 / (this.resources
            .displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT))

        Glide.with(binding.albumImage)
            .load(track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(roundedCorners))
            .into(binding.albumImage)

        if (track != null) {
            binding.playerTextSongName.text = track.trackName
            binding.playerTextSongArtist.text = track.artistName
            binding.textSongLength.text = track.trackTimeMillis
            binding.textSongAlbum.text = track.collectionName
            binding.textSongYear.text = track.releaseDate
            binding.textSongGenre.text = track.primaryGenreName
            binding.textSongCountry.text = track.country
        }

        playerViewModel.getPlayerState()
            .observe(this) { playerActivityState ->
                when (playerActivityState.playerState) {
                    STATE_PREPARED -> {
                        binding.playerButtonPlay.isEnabled = true
                        binding.playerButtonPlay.setImageResource(R.drawable.play)
                        binding.songPlayTime.setText(R.string.play_time)
                    }
                    STATE_PAUSED -> {
                        binding.playerButtonPlay.setImageResource(R.drawable.play)
                        binding.songPlayTime.text = playerActivityState.timeCode
                    }
                    STATE_PLAYING -> {
                        binding.playerButtonPlay.setImageResource(R.drawable.pause)
                        binding.songPlayTime.text = playerActivityState.timeCode
                    }
                    else -> {
                        binding.playerButtonPlay.isEnabled = false
                        binding.playerButtonPlay.setImageResource(R.drawable.play)
                        binding.songPlayTime.setText(R.string.play_time)
                    }
                }
            }
        binding.playerButtonPlay.setOnClickListener {
            (playerViewModel).playbackControl()
        }
        binding.backButtonPlayerAct.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        (playerViewModel).pauseMediaPlayer()
    }

}