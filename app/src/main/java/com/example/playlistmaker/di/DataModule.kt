package com.example.playlistmaker.di

import com.example.playlistmaker.data.models.TrackSharedPref
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TrackNetworkRepositoryImp
import com.example.playlistmaker.data.player.PlayerRepositoryImp
import com.example.playlistmaker.data.shares_pref.ThemeSharedPrefRepositoryImp
import com.example.playlistmaker.data.shares_pref.TrackSharedPreRepositoryImp
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.api.ThemeSharedPrefRepository
import com.example.playlistmaker.domain.api.TrackNetworkRepository
import com.example.playlistmaker.domain.api.TrackSharedPrefRepository
import org.koin.dsl.module

val dataModules = module{
    single<TrackSharedPrefRepository> {
        TrackSharedPreRepositoryImp(context = get())
    }

    single<ThemeSharedPrefRepository> {
        ThemeSharedPrefRepositoryImp(context = get())
    }

    single<TrackNetworkRepository> {
        TrackNetworkRepositoryImp(networkClient = get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(context = get())
    }
    single<PlayerRepository> {
        PlayerRepositoryImp()
    }
}