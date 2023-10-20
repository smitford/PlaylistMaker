package com.example.playlistmaker.domain.use_cases.implementation

import com.example.playlistmaker.domain.api.InternalDataChangerRepository
import com.example.playlistmaker.domain.use_cases.DeleteImageUseCase

class DeleteImageUseCaseImp(val repository: InternalDataChangerRepository) : DeleteImageUseCase {

    override fun execute(uri: String) {
        repository.deleteImg(uri = uri)
    }
}