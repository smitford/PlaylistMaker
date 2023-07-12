package com.example.playlistmaker.data.network


import com.example.playlistmaker.data.models.TrackSearchRequest
import com.example.playlistmaker.data.models.TrackSearchResponse
import com.example.playlistmaker.data.models.adapterTrackDto
import com.example.playlistmaker.domain.api.TrackNetworkRepository
import com.example.playlistmaker.domain.consumer.DataConsumer
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

class TrackNetworkRepositoryImp(private val networkClient: NetworkClient) : TrackNetworkRepository {

    override fun searchTracks(term: String): DataConsumer<List<Track>> {
        val response =
            networkClient.doRequest(TrackSearchRequest(term))

        return if (response.resultCode == 200) {
            val track = (response as TrackSearchResponse).results
            DataConsumer.Success(adapterTrackDto.trackDtoToTrack(track))
        } else DataConsumer.Error(response.resultCode)

    }
}