package com.example.playlistmaker.ui.editPlaylist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.createPlaylist.AllStates
import com.example.playlistmaker.ui.createPlaylist.CreatePlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class EditPlaylistFragment : CreatePlaylistFragment() {
    private val arg: EditPlaylistFragmentArgs by navArgs()
    override val viewModel: EditPlaylistViewModel by viewModel {
        parametersOf(arg.playlistId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewChanger()

        viewModel.getEditPlaylistState().observe(viewLifecycleOwner) { playlistInfo ->
            binding.editTextName.setText(playlistInfo.name)
            binding.editTextDescription.setText(playlistInfo.description)
            playlistInfo.imgUri?.let { image ->
                Glide.with(requireContext())
                    .load(image)
                    .into(binding.playListImg)
            }
        }

    }

    private fun viewChanger() {
        binding.textViewScreenNewPlist.text = getText(R.string.edit_playlist)
        binding.buttonCreate.text = getText(R.string.save)
    }

    override fun initSnack() = Unit

    override fun backBehavior(state: AllStates) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        binding.backButtonNewPlaylist.setOnClickListener { findNavController().popBackStack() }
    }

    override fun saveImageToStorage() {
        val uri = viewModel.getCurrentData().value?.uri
        val uriStart = viewModel.getEditPlaylistState().value?.imgUri

        if (uri == null) {
            return
        } else {
            if (uri == uriStart) {
                return
            } else {
                viewModel.saveImgToStorage(requireContext(), uri)
                uriStart?.let { viewModel.deleteImgFromStorage(it) }

            }
        }
    }
}