package com.example.playlistmaker.ui.media

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.TrackrecycleviewItemBinding
import com.example.playlistmaker.domain.models.Track


class ViewHolder(private val binding: TrackrecycleviewItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        binding.songName.text = track.trackName
        binding.length.text = track.trackTimeMillis
        binding.artistName.text = track.artistName

        Glide.with(binding.albumImage)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(2))
            .into(binding.albumImage)
    }
}