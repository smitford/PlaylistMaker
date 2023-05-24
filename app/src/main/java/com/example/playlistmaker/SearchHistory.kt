package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.trackRecycleView.Track
import com.google.gson.Gson

object SearchHistory {
    lateinit var sharedPreferences: SharedPreferences

    private val gson = Gson()

    var history: String? = ""

    private var historyList = mutableListOf<Track>()

    fun notEmpty () : Boolean = historyList.any()

    private fun getTracksFromHistory () :MutableList<Track> =
       gson.fromJson(
            history.toString(),
            Array<Track>::class.java,
        )
            .toMutableList()

    fun clearHistory () {
        sharedPreferences.edit().remove(SEARCH_HISTORY).apply()
        historyList.clear()
    }

    fun refreshHistory () {
        history = sharedPreferences.getString(SEARCH_HISTORY, "")
        historyList = if(history!="") getTracksFromHistory() else mutableListOf()
    }

    fun getHistory(): MutableList<Track> = historyList


    private fun checkForCoincidence(track: Track): Boolean {
        historyList.forEach { if (it.trackId==track.trackId) return true }
        return false
    }

    private fun addTrack(track: Track) {
        historyList.apply {
            this.reverse()
            this.add(track)
            this.reverse()
        }

    }

    private fun checkLength(): Boolean = this.historyList.count() < 10

    private fun addToSearchHistory(track: Track) {

        if (checkForCoincidence(track)) {
            historyList.remove(track)
            addTrack(track)
            return
        }

        if (checkLength()) {
            addTrack(track)
        } else {
            historyList.removeAt(9)
            addTrack(track)
        }
    }

    private fun getJSON(): String = gson.toJson(historyList)


    fun saveHistory(track: Track) {

        addToSearchHistory(track)
        sharedPreferences.edit().putString(SEARCH_HISTORY, getJSON()).apply()

    }


}




