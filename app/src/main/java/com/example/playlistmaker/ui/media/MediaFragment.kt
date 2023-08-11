package com.example.playlistmaker.ui.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaBinding

class MediaFragment : Fragment() {
    companion object {
        private const val IS_PlAY_LIST = "isPlayList"
        fun newInstance(isPlayList: Boolean) = MediaFragment().apply {
            arguments = Bundle().apply {
                putBoolean(IS_PlAY_LIST, isPlayList)
            }
        }
    }

    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireArguments().getBoolean(IS_PlAY_LIST)
            .apply {
                binding.buttonNewPlayList.visibility = if (this) View.VISIBLE else View.GONE
                    if (this) binding.textviewMediaEmpty.setText(R.string.non_playlists)
                    else binding.textviewMediaEmpty.setText(R.string.empty_media)
            }
    }


}