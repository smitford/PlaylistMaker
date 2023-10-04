package com.example.playlistmaker.ui.media.playlists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsCatalogBinding
import com.example.playlistmaker.domain.models.PlaylistInfo
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsCatalogFragment : Fragment() {
    private val playlistCatalogViewModel by viewModel<PlaylistsCatalogViewModel>()
    private var _binding: FragmentPlaylistsCatalogBinding? = null
    private val binding get() = _binding!!
    private lateinit var catalogRecyclerView: RecyclerView
    private lateinit var catalogAdapter: CatalogAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistCatalogViewModel.loadPlaylistCatalog()

        catalogRecyclerView = binding.recyclerViewCatalog
        catalogRecyclerView.layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
        catalogAdapter = CatalogAdapter()
        catalogRecyclerView.adapter = catalogAdapter

        binding.buttonNewPlayList.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment2)
        }

        playlistCatalogViewModel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistCatalogState.Empty -> {
                    changeVisualState(CatalogStateVisual.EMPTY)
                }

                is PlaylistCatalogState.LoadedCatalog -> {
                    refreshCatalog(state.playlists)
                    changeVisualState(CatalogStateVisual.SHOW_CATALOG)
                }
            }
        }

    }

    private fun refreshCatalog(catalog: List<PlaylistInfo>) {
        catalogAdapter.catalog = catalog.toMutableList()
        catalogAdapter.notifyDataSetChanged()
        Log.d("refresh catalog", "$catalog")
    }

    private fun changeVisualState(state: CatalogStateVisual) {
        when (state) {
            CatalogStateVisual.EMPTY -> {
                binding.recyclerViewCatalog.visibility = View.GONE
                binding.imageviewMediaEmpty.visibility = View.VISIBLE
                binding.textviewMediaEmpty.visibility = View.VISIBLE
            }

            CatalogStateVisual.SHOW_CATALOG -> {
                binding.recyclerViewCatalog.visibility = View.VISIBLE
                binding.imageviewMediaEmpty.visibility = View.GONE
                binding.textviewMediaEmpty.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistsCatalogFragment()
        const val SPAN_COUNT = 2
    }
}

enum class CatalogStateVisual {
    EMPTY,
    SHOW_CATALOG
}