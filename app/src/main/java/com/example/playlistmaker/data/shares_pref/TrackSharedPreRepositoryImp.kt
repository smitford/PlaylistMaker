package com.example.playlistmaker.data.shares_pref

import android.content.Context
import com.example.playlistmaker.data.models.TrackSharedPref
import com.example.playlistmaker.domain.api.TrackSharedPrefRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import sharedPreferencesInit

const val SEARCH_HISTORY = "search_history"

class TrackSharedPreRepositoryImp(context: Context) : TrackSharedPrefRepository {

    private val sharePref = sharedPreferencesInit(context)
    private val gson = Gson()

    override fun getTrackList(): List<Track> {
        val history: String? = sharePref.getString(SEARCH_HISTORY, "")
        val trackList = try {gson.fromJson(
            history.toString(),
            Array<TrackSharedPref>::class.java,).toList()

        }
        catch (e: Exception) {
            listOf()
        }

        return AdapterTrackSharedPref.trackSharedToTrack(trackList)
    }

    override fun saveTrackList(track: Track) {

        val history: String? = sharePref.getString(SEARCH_HISTORY, "")
        val historyList = try {gson.fromJson(
            history.toString(),
            Array<TrackSharedPref>::class.java,).toMutableList()

        }
        catch (e: Exception) {
            mutableListOf()
        }
        val trackNew = AdapterTrackSharedPref.trackToTrackShared(track)

        addToSearchHistory(historyList, trackNew)
        sharePref.edit().putString(
            com.example.playlistmaker.ui.SEARCH_HISTORY,
            gson.toJson(historyList)
        ).apply()
    }

    override fun clearHistory() {
        sharePref.edit().remove(com.example.playlistmaker.ui.SEARCH_HISTORY).apply()
    }

    private fun addToSearchHistory(
        historyList: MutableList<TrackSharedPref>,
        track: TrackSharedPref
    ): MutableList<TrackSharedPref> {
        if (checkForCoincidence(historyList, track)) {
            historyList.remove(track)
            addTrack(historyList, track)
            return historyList
        }
        return if (checkLength(historyList)) {
            addTrack(historyList, track)
        } else {
            historyList.removeAt(9)
            addTrack(historyList, track)
        }
    }

    private fun checkForCoincidence(
        historyList: MutableList<TrackSharedPref>,
        track: TrackSharedPref
    ): Boolean {
        historyList.forEach { if (it.trackId == track.trackId) return true }
        return false
    }

    private fun checkLength(historyList: MutableList<TrackSharedPref>): Boolean =
        historyList.count() < 10

    private fun addTrack(
        historyList: MutableList<TrackSharedPref>,
        track: TrackSharedPref
    ): MutableList<TrackSharedPref> =
        historyList.apply {
            this.reverse()
            this.add(track)
            this.reverse()
        }
}
