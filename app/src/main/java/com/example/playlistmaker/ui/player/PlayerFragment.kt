package com.example.playlistmaker.ui.player


import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel



class PlayerFragment : Fragment() {
    private val args: PlayerFragmentArgs by navArgs()
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private val playerViewModel by viewModel<PlayerViewModel>()
    private lateinit var recyclerViewPlaylist: RecyclerView
    private lateinit var playerCatalogAdapter: PlayerCatalogAdapter

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
        val bottomSheetContainer = binding.bottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.isHideable = true

        val roundedCorners = (ROUNDING_OF_CORNERS_PX * (requireContext().resources
            .displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT))

        val addTrackToPlaylistCallBack = { track: Track, playlistPK: Int ->
            playerViewModel.addTrackToPlaylist(playlistPK = playlistPK, track = track)

            viewLifecycleOwner.lifecycleScope.launch {
                delay(TOAST_DEBOUNCE_MSC)
                val status = playerViewModel.getAddStatus().value
                Log.d("Call back status", "$status")
                val snackText = if (status == true) {
                    "Добавлено в плейлист ${playerViewModel.getPlayerName(playlistPK)}"
                } else {
                    "Трек уже добавлен в плейлист ${playerViewModel.getPlayerName(playlistPK)}"
                }
                initSnack(snackText)
            }

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        recyclerViewPlaylist = binding.recyclerViewSongs
        recyclerViewPlaylist.layoutManager = LinearLayoutManager(requireContext())
        playerCatalogAdapter = PlayerCatalogAdapter(
            addTrackToPlaylist = addTrackToPlaylistCallBack,
            track = track
        )
        recyclerViewPlaylist.adapter = playerCatalogAdapter

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

        playerViewModel.checkForFavorite(trackID = track.trackId)

        playerViewModel.prepare(track)

        playerViewModel.getPlayerState()
            .observe(viewLifecycleOwner) { playerActivityState ->
                if (playerActivityState.isFavorite) {
                    binding.playerButtonLike.setImageResource(R.drawable.like_button)
                } else {
                    binding.playerButtonLike.setImageResource(R.drawable.like)
                }
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

        playerViewModel.getPlaylistCatalogState().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                playerCatalogAdapter.catalog = result.toMutableList()
                playerCatalogAdapter.notifyDataSetChanged()
            }

        }


        val bottomSheetCallback = object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HALF_EXPANDED, BottomSheetBehavior.STATE_DRAGGING,
                    BottomSheetBehavior.STATE_SETTLING -> binding.screenDimming.visibility =
                        View.VISIBLE

                    else -> binding.screenDimming.visibility = View.GONE
                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        }
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)

        binding.playerButtonPlay.setOnClickListener {
            (playerViewModel).playbackControl()
        }
        binding.backButtonPlayerAct.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.playerButtonLike.setOnClickListener {
            playerViewModel.changeStatus(track = track)
        }
        binding.playerButtonAdd.setOnClickListener {
            playerViewModel.loadPlaylistCatalog()
            bottomSheetBehavior.halfExpandedRatio = 0.65F
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_createPlaylistFragment)
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

    private fun initSnack(snackText: String) {
        val createdSnack =
            Snackbar.make(binding.recyclerViewSongs, snackText, Snackbar.LENGTH_SHORT)
        createdSnack
            .setTextMaxLines(1)
            .show()

    }

    companion object {
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val ROUNDING_OF_CORNERS_PX = 8
        private const val TOAST_DEBOUNCE_MSC = 200L
    }
}