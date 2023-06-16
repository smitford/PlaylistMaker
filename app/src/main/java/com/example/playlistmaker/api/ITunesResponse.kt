package com.example.playlistmaker.api

import com.example.playlistmaker.trackrecycleview.Track

data class ITunesResponse (val resultCount : Int, val results: List<Track>) {
}