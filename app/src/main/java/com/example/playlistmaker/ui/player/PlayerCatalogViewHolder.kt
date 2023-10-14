package com.example.playlistmaker.ui.player

import android.util.DisplayMetrics
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistRecycleviewSmallItemBinding
import com.example.playlistmaker.domain.models.PlaylistInfo

class PlayerCatalogViewHolder(private val binding: PlaylistRecycleviewSmallItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(playlistInfo: PlaylistInfo) {

        binding.playlistName.text = playlistInfo.name
        binding.trackCount.text = playlistInfo.tracksNumber.toString() + " треков"

        Glide.with(binding.playlistImg)
            .load(playlistInfo.imgUri)
            .placeholder(R.drawable.placeholder)
            .apply(RequestOptions())
            .centerCrop()
            .transform(RoundedCorners(roundCorner()))
            .into(binding.playlistImg)

    }

    private fun roundCorner(): Int = ROUNDING_OF_CORNERS_PX * (itemView.resources
        .displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)

    companion object {
        private const val ROUNDING_OF_CORNERS_PX = 8
    }
}