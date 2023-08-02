package com.example.playlistmaker.di

import com.example.playlistmaker.domain.use_cases.PlayerInteractor
import com.example.playlistmaker.domain.use_cases.ThemeInteractor
import com.example.playlistmaker.domain.use_cases.TrackClearHistoryUseCase
import com.example.playlistmaker.domain.use_cases.TrackGetUseCase
import com.example.playlistmaker.domain.use_cases.TrackSaveUseCase
import com.example.playlistmaker.domain.use_cases.TrackSearchUseCase
import com.example.playlistmaker.domain.use_cases.implementation.PlayerInteractorImp
import com.example.playlistmaker.domain.use_cases.implementation.ThemeInteractorImp
import com.example.playlistmaker.domain.use_cases.implementation.TrackClearHistoryUseCaseImp
import com.example.playlistmaker.domain.use_cases.implementation.TrackGetUseCaseImp
import com.example.playlistmaker.domain.use_cases.implementation.TrackSaveUseCaseImp
import com.example.playlistmaker.domain.use_cases.implementation.TrackSearchUseCaseImp
import org.koin.dsl.module

val domainModules = module {
    factory<PlayerInteractor> {
        PlayerInteractorImp(repository = get())
    }
    factory<ThemeInteractor> {
        ThemeInteractorImp(repository = get())
    }
    factory<TrackClearHistoryUseCase> {
        TrackClearHistoryUseCaseImp(repository = get())
    }
    factory<TrackGetUseCase> {
        TrackGetUseCaseImp(repository = get())
    }
    factory<TrackSaveUseCase> {
        TrackSaveUseCaseImp(repository = get())
    }
    factory<TrackSearchUseCase> {
        TrackSearchUseCaseImp(repository = get())
    }
}