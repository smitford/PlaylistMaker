package com.example.playlistmaker.ui.media.playlists

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistrecycleviewItemBinding
import com.example.playlistmaker.domain.models.PlaylistInfo

class CatalogViewHolder(private val binding: PlaylistrecycleviewItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(playlistInfo: PlaylistInfo) {
        binding.playlistName.text = playlistInfo.name
        binding.trackCount.text = playlistInfo.tracksNumber.toString() + " треков"

        Glide.with(binding.playListImg)
            .load(playlistInfo.imgUri)
            .placeholder(R.drawable.placeholder)
            .apply(RequestOptions())
            .centerInside()
            .into(binding.playListImg)

    }
}