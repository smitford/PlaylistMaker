package com.example.playlistmaker.data.network


import com.example.playlistmaker.data.models.TrackSearchRequest
import com.example.playlistmaker.data.models.TrackSearchResponse
import com.example.playlistmaker.domain.api.TrackNetworkRepository
import com.example.playlistmaker.domain.consumer.DataConsumer
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TrackNetworkRepositoryImp(private val networkClient: NetworkClient) : TrackNetworkRepository {

    override fun searchTracks(term: String): Flow<DataConsumer<List<Track>>> = flow {
        val response =
            networkClient.doRequest(TrackSearchRequest(term))

        if (response.resultCode == 200) {
            val track = (response as TrackSearchResponse).results
            emit(DataConsumer.Success(AdapterTrackDto.trackDtoToTrack(track)))
        } else emit(DataConsumer.Error(response.resultCode))

    }.flowOn(Dispatchers.IO)
}