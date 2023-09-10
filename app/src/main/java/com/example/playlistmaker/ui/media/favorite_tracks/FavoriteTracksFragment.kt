package com.example.playlistmaker.ui.media.favorite_tracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.media.AdapterMedia
import com.example.playlistmaker.ui.media.MediaFragmentDirections
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteTracksFragment : Fragment() {
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!
    private val favoriteTracksViewModel by viewModel<FavoriteTracksViewModel>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterMedia: AdapterMedia

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapterCallBack: (Track) -> Unit = debounce(
            delayMillis = DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            val arg = MediaFragmentDirections.actionMediaFragmentToPlayerFragment(track)
            findNavController().navigate(arg)
        }

        favoriteTracksViewModel.updateFavList()

        recyclerView = binding.recyclerViewFavTracks
        adapterMedia = AdapterMedia(adapterCallBack)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapterMedia


        favoriteTracksViewModel.getStatus()
            .observe(viewLifecycleOwner) { favoriteTracksViewModelState ->
                when (favoriteTracksViewModelState) {
                    is FavoriteTracksState.Empty -> {
                        changeState(FavoriteTracksItemsVisibility.EMPTY)
                    }

                    is FavoriteTracksState.HasTracks -> {
                        changeState(FavoriteTracksItemsVisibility.HAS_TRACKS)
                        adapterMedia.tracks = favoriteTracksViewModelState.tracks.toMutableList()
                        adapterMedia.notifyDataSetChanged()
                    }
                }
            }
    }

    private fun changeState(state: FavoriteTracksItemsVisibility) {
        when (state) {
            FavoriteTracksItemsVisibility.EMPTY -> {
                binding.imageviewMediaEmpty.visibility = View.VISIBLE
                binding.textviewMediaEmpty.visibility = View.VISIBLE
                binding.recyclerViewFavTracks.visibility = View.GONE
            }

            FavoriteTracksItemsVisibility.HAS_TRACKS -> {
                binding.imageviewMediaEmpty.visibility = View.GONE
                binding.textviewMediaEmpty.visibility = View.GONE
                binding.recyclerViewFavTracks.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        fun newInstance() = FavoriteTracksFragment()
        const val DEBOUNCE_DELAY = 500L
    }
}

enum class FavoriteTracksItemsVisibility {
    EMPTY,
    HAS_TRACKS
}

