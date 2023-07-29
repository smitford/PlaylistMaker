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
import com.example.playlistmaker.domain.use_cases.PlayerInteractor
import com.example.playlistmaker.domain.use_cases.TrackClearHistoryUseCase
import com.example.playlistmaker.domain.use_cases.TrackGetUseCase
import com.example.playlistmaker.domain.use_cases.TrackSaveUseCase
import com.example.playlistmaker.domain.use_cases.TrackSearchUseCase
import com.example.playlistmaker.domain.use_cases.ThemeInteractor

object Creator {

    fun getTrackClearHistoryUseCase(context: Context): TrackClearHistoryUseCase =
        TrackClearHistoryUseCase(getTrackSharedPrefRepo(context))

    fun getTrackGetUseCase(context: Context): TrackGetUseCase =
        TrackGetUseCase(getTrackSharedPrefRepo(context))

    fun getTrackSaveUseCase(context: Context): TrackSaveUseCase =
        TrackSaveUseCase(getTrackSharedPrefRepo(context))

    private fun getTrackSharedPrefRepo(context: Context): TrackSharedPrefRepository =
        TrackSharedPreRepositoryImp(context)

    fun getTrackSearchUseCase(context: Context): TrackSearchUseCase = TrackSearchUseCase(
        repository = getRepository(context = context)
    )

    private fun getRepository(context: Context): TrackNetworkRepositoryImp =
        TrackNetworkRepositoryImp(
            getRetrofitNetworkClient(context = context)
        )

    private fun getRetrofitNetworkClient(context: Context): RetrofitNetworkClient =
        RetrofitNetworkClient(context = context)

    fun getPlayerInteractor(): PlayerInteractor = PlayerInteractor(getPlayerRepository())
    private fun getPlayerRepository(): PlayerRepository = PlayerRepositoryImp()

    fun getSaveThemeUseCase(context: Context): ThemeInteractor =
        ThemeInteractor(getThemeSharedPrefRepository(context = context))

    private fun getThemeSharedPrefRepository(context: Context): ThemeSharedPrefRepository =
        ThemeSharedPrefRepositoryImp(context = context)


}