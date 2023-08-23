package com.example.playlistmaker.ui.player


import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerFragment : Fragment() {
    private val args: PlayerFragmentArgs by navArgs()
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private val playerViewModel by viewModel<PlayerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = args.track

        val roundedCorners = (8 / (this.resources
            .displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT))

        Glide.with(binding.albumImage)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(roundedCorners))
            .into(binding.albumImage)


        binding.playerTextSongName.text = track.trackName
        binding.playerTextSongArtist.text = track.artistName
        binding.textSongLength.text = track.trackTimeMillis
        binding.textSongAlbum.text = track.collectionName
        binding.textSongYear.text = track.releaseDate
        binding.textSongGenre.text = track.primaryGenreName

        playerViewModel.prepare(track)

        playerViewModel.getPlayerState()
            .observe(viewLifecycleOwner) { playerActivityState ->
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

            findNavController().popBackStack()

        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pauseMediaPlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}