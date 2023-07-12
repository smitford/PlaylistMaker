package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.shares_pref.TrackSharedPreRepositoryImp
import com.example.playlistmaker.domain.api.TrackSharedPrefRepository
import com.example.playlistmaker.domain.use_cases.TrackClearHistoryUseCase
import com.example.playlistmaker.domain.use_cases.TrackGetUseCase
import com.example.playlistmaker.domain.use_cases.TrackSaveUseCase

object Creator {

    private fun getTrackSharedPrefRepo(context: Context): TrackSharedPrefRepository =
        TrackSharedPreRepositoryImp(context)

    fun getTrackClearHistoryUseCase(context: Context): TrackClearHistoryUseCase =
        TrackClearHistoryUseCase(getTrackSharedPrefRepo(context))

    fun getTrackGetUseCase(context: Context): TrackGetUseCase =
        TrackGetUseCase(getTrackSharedPrefRepo(context))

    fun getTrackSaveUseCase(context: Context): TrackSaveUseCase =
        TrackSaveUseCase(getTrackSharedPrefRepo(context))
}