package com.example.playlistmaker.trackRecycleView

import java.text.SimpleDateFormat
import java.util.*

class Track(
    val trackName: String,
    val artistName: String,
    var trackTimeMillis: String,
    val artworkUrl100: String
) {
    fun changeFormat(){
        this.trackTimeMillis = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis.toLong())
    }

}