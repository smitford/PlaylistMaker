package com.example.playlistmaker.trackRecycleView

import java.text.SimpleDateFormat
import java.util.*

class Track(
    val trackName: String,
    val artistName: String,
    var trackTimeMillis: String,
    val artworkUrl100: String,
    val trackId: Int
) {
    fun changeFormat() {
        this.trackTimeMillis =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis.toLong())
    }

    override fun equals(other: Any?): Boolean =
        (other is Track) && this.trackId == other.trackId && other.trackId == this.trackId


}