package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TrackNetworkRepositoryImp
import com.example.playlistmaker.data.player.PlayerRepositoryImp
import com.example.playlistmaker.data.shares_pref.ThemeSharedPrefRepositoryImp
import com.example.playlistmaker.data.shares_pref.TrackSharedPreRepositoryImp
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.api.ThemeSharedPrefRepository
import com.example.playlistmaker.domain.api.TrackSharedPrefRepository
import com.example.playlistmaker.domain.use_cases.implementation.PlayerInteractorImp
import com.example.playlistmaker.domain.use_cases.implementation.TrackClearHistoryUseCaseImp
import com.example.playlistmaker.domain.use_cases.implementation.TrackGetUseCaseImp
import com.example.playlistmaker.domain.use_cases.implementation.TrackSaveUseCaseImp
import com.example.playlistmaker.domain.use_cases.implementation.TrackSearchUseCaseImp
import com.example.playlistmaker.domain.use_cases.implementation.ThemeInteractorImp

object Creator {

    fun getTrackClearHistoryUseCase(context: Context): TrackClearHistoryUseCaseImp =
        TrackClearHistoryUseCaseImp(getTrackSharedPrefRepo(context))

    fun getTrackGetUseCase(context: Context): TrackGetUseCaseImp =
        TrackGetUseCaseImp(getTrackSharedPrefRepo(context))

    fun getTrackSaveUseCase(context: Context): TrackSaveUseCaseImp =
        TrackSaveUseCaseImp(getTrackSharedPrefRepo(context))

    private fun getTrackSharedPrefRepo(context: Context): TrackSharedPrefRepository =
        TrackSharedPreRepositoryImp(context)

    fun getTrackSearchUseCase(context: Context): TrackSearchUseCaseImp = TrackSearchUseCaseImp(
        repository = getRepository(context = context)
    )

    private fun getRepository(context: Context): TrackNetworkRepositoryImp =
        TrackNetworkRepositoryImp(
            getRetrofitNetworkClient(context = context)
        )

    private fun getRetrofitNetworkClient(context: Context): RetrofitNetworkClient =
        RetrofitNetworkClient(context = context)

    fun getPlayerInteractor(): PlayerInteractorImp = PlayerInteractorImp(getPlayerRepository())
    private fun getPlayerRepository(): PlayerRepository = PlayerRepositoryImp()

    fun getSaveThemeUseCase(context: Context): ThemeInteractorImp =
        ThemeInteractorImp(getThemeSharedPrefRepository(context = context))

    private fun getThemeSharedPrefRepository(context: Context): ThemeSharedPrefRepository =
        ThemeSharedPrefRepositoryImp(context = context)


}