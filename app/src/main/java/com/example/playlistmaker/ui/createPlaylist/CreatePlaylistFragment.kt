package com.example.playlistmaker.ui.createPlaylist

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class CreatePlaylistFragment : Fragment() {
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private val createPlaylistVM by viewModel<CreatePlaylistViewModel>()
    private lateinit var inputMethodManager: InputMethodManager
    private lateinit var dialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val state = createPlaylistVM.getCurrentData().value
        if (state?.state == AllStates.SAVED_DATA) {
            binding.playListImg.setImageURI(state.uri?.toUri())
            binding.editTextName.setText(state.playlistName)
            binding.editTextDescription.setText(state.description)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var descriptionJob: Job? = null
        val pickPlaylistImg =
            registerForActivityResult(
                ActivityResultContracts.PickVisualMedia()
            ) { uri ->
                if (uri != null) {
                    binding.playListImg.scaleType = ImageView.ScaleType.CENTER_CROP
                    binding.playListImg.setImageURI(uri)
                    createPlaylistVM.saveImg(uri.toString())
                } else {
                    Log.d("ImgPicker", "Not chose")
                }
            }

        dialog =
            MaterialAlertDialogBuilder(requireContext(), R.style.dialogStyle)
                .setTitle(R.string.complete_playlist_creation)
                .setMessage(R.string.completion_dialog)
                .setNegativeButton(R.string.cancel) { _, _ ->

                }
                .setPositiveButton(R.string.complete) { _, _ ->
                    findNavController().popBackStack()
                }

        binding.playListImg.setOnClickListener {
            pickPlaylistImg.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        createPlaylistVM.getCurrentData().observe(viewLifecycleOwner) { currentState ->

            backBehavior(currentState.state)

            when (currentState.state) {
                AllStates.START -> {
                    binding.buttonCreate.isEnabled = false
                    binding.buttonCreate.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey1
                        )
                    )
                }

                AllStates.SAVED_DATA -> {}

                AllStates.SAVED_PLAYLIST -> {
                    findNavController().popBackStack()
                }
            }
        }

        val editTextDescriptionChList = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                descriptionJob?.cancel()
                descriptionJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(DEBOUNCE_DELAY_MSC)
                    createPlaylistVM.saveDescription(p0.toString())
                }
                activateEditTextStateChanger(binding.textInputLayoutDescription, p0.isNullOrEmpty())
            }

            override fun afterTextChanged(p0: Editable?) = Unit
        }
        val editTextNameChList = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                descriptionJob?.cancel()
                descriptionJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(DEBOUNCE_DELAY_MSC)
                    createPlaylistVM.saveName(p0.toString())
                }
                activateEditTextStateChanger(binding.textInputLayoutName, p0.isNullOrEmpty())
                activateButtonSaveStateChanger(p0.isNullOrBlank())
            }

            override fun afterTextChanged(p0: Editable?) = Unit
        }

        binding.editTextName.addTextChangedListener(editTextNameChList)

        binding.editTextDescription.addTextChangedListener(editTextDescriptionChList)

        binding.editTextDescription.setOnEditorActionListener { _, actionID, _ ->
            if (actionID == EditorInfo.IME_ACTION_DONE) {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                binding.buttonCreate.requestFocus()
                true
            } else false
        }

        binding.editTextName.setOnEditorActionListener { _, actionID, _ ->
            if (actionID == EditorInfo.IME_ACTION_DONE) {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                binding.editTextDescription.requestFocus()
                true
            } else false
        }

        binding.editTextName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                Log.d("ChangeListenerName", "Saved")
                createPlaylistVM.saveName(binding.editTextName.text.toString())
            }
            activateEditTextStateChanger(
                binding.textInputLayoutName,
                binding.editTextName.text.isNullOrBlank() && !hasFocus
            )
        }
        binding.editTextDescription.setOnFocusChangeListener { _, hasFocus ->
            activateEditTextStateChanger(
                binding.textInputLayoutDescription,
                binding.editTextDescription.text.isNullOrBlank() && !hasFocus
            )

        }

        binding.buttonCreate.setOnClickListener {
            lifecycleScope.launch { createPlaylistVM.savePlaylist() }
            saveImageToStorage()
            initSnack()
        }
    }

    private fun activateButtonSaveStateChanger(hasName: Boolean) {
        Log.d("Button", "Changed shape")
        binding.buttonCreate.isEnabled = !hasName
        if (hasName) {
            binding.buttonCreate.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_button_enadble)
        } else {
            binding.buttonCreate.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_button_able)
        }
    }

    private fun activateEditTextStateChanger(view: TextInputLayout, isBlank: Boolean) {
        val colorHint = if (isBlank) {
            ContextCompat.getColorStateList(requireContext(), R.color.grey1)
        } else {
            ContextCompat.getColorStateList(requireContext(), R.color.blue1)
        }
        val selector = if (isBlank) {
            ContextCompat.getColorStateList(requireContext(), R.color.box_stroke_selector_grey)
        } else {
            ContextCompat.getColorStateList(requireContext(), R.color.box_stroke_selector)
        }

        view.defaultHintTextColor = colorHint
        selector?.let { view.setBoxStrokeColorStateList(it) }
    }


    private fun backBehavior(state: AllStates) {
        when (state) {
            AllStates.SAVED_DATA -> {
                binding.backButtonNewPlaylist.setOnClickListener { dialog.show() }
                requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                    dialog.show()
                }
            }

            else -> {
                binding.backButtonNewPlaylist.setOnClickListener { findNavController().popBackStack() }
                requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                    findNavController().popBackStack()
                }
            }
        }
    }


    private fun saveImageToStorage() {
        val uri = createPlaylistVM.getCurrentData().value?.uri?.toUri()
        if (uri == null) {
            return
        } else {
            val filePath = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                PLAYLIST_IMAGE_DIRECTORY
            )
            if (!filePath.exists()) {
                filePath.mkdirs()
            }
            val file = File(
                filePath,
                createPlaylistVM.getCurrentData().value?.playlistName + Calendar.getInstance().timeInMillis
            )
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            BitmapFactory.decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 20, outputStream)
        }
    }

    private fun initSnack() {
        val snackText = "Плейлист ${createPlaylistVM.getCurrentData().value?.playlistName} создан"
        val createdSnack = make(binding.buttonCreate, snackText, Snackbar.LENGTH_SHORT)
        createdSnack
            .setTextMaxLines(1)
            .show()
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


