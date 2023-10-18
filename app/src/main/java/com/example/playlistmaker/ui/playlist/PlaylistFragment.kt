package com.example.playlistmaker.ui.playlist

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.PlaylistInfo
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.media.favorite_tracks.FavoriteTracksFragment
import com.example.playlistmaker.ui.search.ViewHolderOfSongs
import com.example.playlistmaker.utils.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistFragment : Fragment() {
    private val args: PlaylistFragmentArgs by navArgs()
    private val playlistViewModel by viewModel<PlaylistViewModel>()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlaylistAdapter
    private lateinit var dialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistId = args.playlistId
        val bottomSheetContainer = binding.bottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        val bottomSheetContainerMenu = binding.bottomSheetMenu
        val bottomSheetBehaviorMenu = BottomSheetBehavior.from(bottomSheetContainerMenu)


        val callBackMoveToPlayer = debounce(
            delayMillis = FavoriteTracksFragment.DEBOUNCE_DELAY_MILS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track: Track ->
            val arg = PlaylistFragmentDirections.actionPlaylistFragmentToPlayerFragment(track)
            findNavController().navigate(arg)
        }

        val callBackDeleteTrack = { trackId: Int ->
            dialog =
                MaterialAlertDialogBuilder(requireContext(), R.style.dialogStyle)
                    .setTitle(R.string.complete_playlist_creation)
                    .setMessage(R.string.delete_track)
                    .setNegativeButton(R.string.no) { _, _ ->
                    }
                    .setPositiveButton(R.string.yes) { _, _ ->
                        playlistViewModel.deleteTrackFromPlaylist(trackId = trackId)
                    }
            dialog.show()
            Unit
        }

        adapter = PlaylistAdapter(callBackMoveToPlayer, callBackDeleteTrack)
        recyclerView = binding.recyclerViewSongs
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        bottomSheetBehavior.isHideable = false
        bottomSheetBehaviorMenu.addBottomSheetCallback(initBottomSheetCallBack())
        bottomSheetBehaviorMenu.isHideable = true
        bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN

        lifecycleScope.launch(Dispatchers.Main) {
            bottomSheetBehavior.peekHeight = peekHeightCalculation(binding.imgShear)
        }
        playlistViewModel.getPlaylist(playlistId = playlistId)

        playlistViewModel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistState.FilledPlaylist -> {
                    fullViewState(
                        playlist = state.playlist,
                        playingTime = state.playingTime
                    )
                }

                is PlaylistState.EmptyPlaylist -> {
                    emptyViewState(
                        playlistInfo = state.playlistInfo
                    )
                }

                else -> errorViewState()
            }
        }

        binding.imageViewBackButtonPlaylist.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imgShear.setOnClickListener {
            when (playlistViewModel.getState().value) {
                is PlaylistState.EmptyPlaylist -> showToast()
                is PlaylistState.FilledPlaylist -> playlistViewModel.shearPlaylist(requireActivity())
                else -> Unit
            }
        }
        binding.editButton.setOnClickListener {
            bottomSheetBehaviorMenu.halfExpandedRatio = 0.45F
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.textViewDelete.setOnClickListener { deletePlaylistDialog(playlistId = playlistId) }
        binding.textViewEdit.setOnClickListener {
            val arg =
                PlaylistFragmentDirections.actionPlaylistFragmentToEditPlaylistFragment(playlistId)
            findNavController().navigate(arg)
        }
        binding.textViewShare.setOnClickListener { playlistViewModel.shearPlaylist(requireContext()) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun peekHeightCalculation(imgShare: View): Int {
        val imgShareLocation = IntArray(2)
        imgShare.getLocationInWindow(imgShareLocation)
        return binding.root.height - imgShareLocation[1] - requireContext().resources.getDimensionPixelSize(
            R.dimen.padding_24
        )
    }

    private fun initBottomSheetCallBack() = object :
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

    private fun emptyViewState(playlistInfo: PlaylistInfo) {
        Glide.with(binding.imageViewPlaylistCover)
            .load(playlistInfo.imgUri)
            .placeholder(R.drawable.placeholder)
            .apply(RequestOptions())
            .centerCrop()
            .into(binding.imageViewPlaylistCover)

        Glide.with(binding.playlistImgMenu)
            .load(playlistInfo.imgUri)
            .placeholder(R.drawable.placeholder)
            .apply(RequestOptions())
            .centerCrop()
            .transform(RoundedCorners(roundCorner()))
            .into(binding.playlistImgMenu)

        binding.textViewPlaylistName.text = playlistInfo.name
        binding.textViewDescription.text = playlistInfo.description ?: "-"
        binding.textViewPlaylistLength.setText(R.string.zero_playing_time)
        binding.textViewPlaylistTracksNumber.setText(R.string.zero_track_in_playlist)

        binding.playlistName.text = playlistInfo.name
        binding.trackCount.setText(R.string.zero_track_in_playlist)

        binding.recyclerViewSongs.visibility = View.GONE
        binding.imageviewEmpty.visibility = View.VISIBLE
        binding.textviewEmpty.visibility = View.VISIBLE
    }

    private fun fullViewState(playlist: Playlist, playingTime: Int) {
        Glide.with(binding.imageViewPlaylistCover)
            .load(playlist.playlistInfo.imgUri)
            .placeholder(R.drawable.placeholder)
            .apply(RequestOptions())
            .centerCrop()
            .into(binding.imageViewPlaylistCover)

        Glide.with(binding.playlistImgMenu)
            .load(playlist.playlistInfo.imgUri)
            .placeholder(R.drawable.placeholder)
            .apply(RequestOptions())
            .centerCrop()
            .transform(RoundedCorners(roundCorner()))
            .into(binding.playlistImgMenu)

        binding.textViewPlaylistName.text = playlist.playlistInfo.name
        binding.textViewDescription.text = playlist.playlistInfo.description ?: "-"
        val trackNumber =
            when (playlist.playlistInfo.tracksNumber % 10) {
                1 -> playlist.playlistInfo.tracksNumber.toString() + " " + getString(R.string.one_track)
                2, 3, 4 -> playlist.playlistInfo.tracksNumber.toString() + " " + getString(R.string.track)
                else -> playlist.playlistInfo.tracksNumber.toString() + " " + getString(R.string.tracks)
            }
        binding.textViewPlaylistTracksNumber.text =
            when (playingTime % 10) {
                1 -> playingTime.toString() + " " + getString(R.string.one_track)
                2, 3, 4 -> playingTime.toString() + " " + getString(R.string.track)
                else -> playingTime.toString() + " " + getString(R.string.tracks)
            }.toString()


        binding.textViewPlaylistLength.text = trackNumber
        binding.recyclerViewSongs.visibility = View.VISIBLE
        binding.imageviewEmpty.visibility = View.GONE
        binding.textviewEmpty.visibility = View.GONE

        binding.playlistName.text = playlist.playlistInfo.name
        binding.trackCount.text = trackNumber

        adapter.tracks = playlist.tracks.toMutableList()
        adapter.notifyDataSetChanged()

    }

    private fun showToast() {
        Toast.makeText(context, getText(R.string.share_toast_empty_playlist), Toast.LENGTH_LONG)
            .show()
    }

    private fun deletePlaylistDialog(playlistId: Int) {
        dialog =
            MaterialAlertDialogBuilder(requireContext(), R.style.dialogStyle)
                .setTitle(R.string.complete_playlist_creation)
                .setMessage(R.string.delete_track)
                .setNegativeButton(R.string.no) { _, _ ->
                }
                .setPositiveButton(R.string.yes) { _, _ ->
                    playlistViewModel.deletePlaylist(playlistId = playlistId)
                    lifecycleScope.launch {
                        delay(DELETE_DELAY_MSC)
                        findNavController().popBackStack()
                    }
                }
        dialog.show()
    }

    private fun errorViewState() {
        binding.imageViewPlaylistCover.setImageResource(R.drawable.placeholder)
    }

    private fun roundCorner(): Int = ROUNDING_OF_CORNERS_PX * (requireContext().resources
        .displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)

    companion object {
        const val ROUNDING_OF_CORNERS_PX = 8
        const val DELETE_DELAY_MSC = 250L
    }


}