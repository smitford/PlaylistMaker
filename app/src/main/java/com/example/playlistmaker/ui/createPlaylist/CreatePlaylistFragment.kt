package com.example.playlistmaker.ui.createPlaylist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistFragment : Fragment() {
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private val createPlaylistVM by viewModel<CreatePlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       val pickPlaylistImg =
           registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    saveImageToStorage(uri = uri)
                } else {
                    Log.d("ImgPicker", "Error")
                }
            }

        binding.playListImg.setOnClickListener {
            pickPlaylistImg.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        createPlaylistVM.getCurrentData().observe(viewLifecycleOwner) { currentState ->
            when (currentState.state) {
                AllStates.START -> {
                    binding.buttonCreate.isEnabled = false
                    binding.buttonCreate.setBackgroundColor(resources.getColor(R.color.grey1))
                }

                AllStates.SAVED_IMG -> {
                    binding.playListImg.setImageURI(currentState.uri.toUri())
                }

                AllStates.SAVED_NAME -> {
                    binding.playListImg.setImageURI(currentState.uri.toUri())
                    binding.editTextName.setText(currentState.playlistName)
                }

                AllStates.SAVED_DESCRIPTION -> {
                    binding.playListImg.setImageURI(currentState.uri.toUri())
                    binding.editTextName.setText(currentState.playlistName)
                    binding.editTextDescription.setText(currentState.description)
                    binding.buttonCreate.isEnabled = true
                    binding.buttonCreate.setBackgroundColor(resources.getColor(R.color.blue1))
                }

                AllStates.SAVED_PLAYLIST -> {

                }
            }
        }

        binding.editTextName.addTextChangedListener(textWatcherCreator(EditTextPurpose.NAME))

        binding.editTextDescription.addTextChangedListener(textWatcherCreator(EditTextPurpose.DESCRIPTION))

        binding.buttonCreate.setOnClickListener {
            createPlaylistVM.savePlaylist()

        }

        binding.backButtonNewPlaylist.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun saveImageToStorage(uri: Uri) {
        val filePath = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLIST_IMAGE_DIRECTORY
        )

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, "")
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        createPlaylistVM.saveImg(file.toURI().toString())

    }

    private fun showToast() {
        Toast.makeText(requireContext(), "Плейлист создан", Toast.LENGTH_LONG)
    }

    private fun textWatcherCreator(purpose: EditTextPurpose) = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            TODO("Not yet implemented")
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (!binding.editTextName.hasFocus())
                when (purpose) {
                    EditTextPurpose.NAME -> viewLifecycleOwner.lifecycleScope.launch {
                        delay(DEBOUNCE_DELAY_MSC)
                        createPlaylistVM.saveName(binding.editTextName.text.toString())
                    }

                    EditTextPurpose.DESCRIPTION -> viewLifecycleOwner.lifecycleScope.launch {
                        delay(DEBOUNCE_DELAY_MSC)
                        createPlaylistVM.saveDescription(binding.editTextName.text.toString())
                    }
                }
        }

        override fun afterTextChanged(p0: Editable?) {
            TODO("Not yet implemented")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val PLAYLIST_IMAGE_DIRECTORY = "playlist_images"
        const val DEBOUNCE_DELAY_MSC = 1000L
    }

}

enum class EditTextPurpose {
    NAME,
    DESCRIPTION
}