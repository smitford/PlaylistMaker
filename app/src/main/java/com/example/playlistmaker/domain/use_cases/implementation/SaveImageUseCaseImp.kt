package com.example.playlistmaker.domain.use_cases.implementation

import android.content.Context
import com.example.playlistmaker.domain.api.InternalDataChangerRepository
import com.example.playlistmaker.domain.use_cases.SaveImageUseCase

class SaveImageUseCaseImp(val repository: InternalDataChangerRepository) : SaveImageUseCase {
    override fun execute(uri: String, context: Context, playlistName: String) {
        repository.saveImg(uri = uri, context = context, playlistName =playlistName)
    }
}