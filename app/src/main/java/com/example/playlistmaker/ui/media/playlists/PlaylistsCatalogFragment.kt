package com.example.playlistmaker.ui.media.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.ui.search.SearchFragment
import com.example.playlistmaker.ui.search.SearchFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsCatalogFragment : Fragment() {
    private val playlistCatalogViewModel by viewModel<PlaylistsCatalogViewModel>()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNewPlayList.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment2)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistsCatalogFragment()
    }
}