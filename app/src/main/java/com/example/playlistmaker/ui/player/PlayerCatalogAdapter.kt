package com.example.playlistmaker.ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistRecycleviewSmallItemBinding
import com.example.playlistmaker.domain.models.PlaylistInfo
import com.example.playlistmaker.domain.models.Track


class PlayerCatalogAdapter(val addTrackToPlaylist: (Track, Int) -> Unit, val track: Track) :
    RecyclerView.Adapter<PlayerCatalogViewHolder>() {

    var catalog = mutableListOf<PlaylistInfo>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerCatalogViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlayerCatalogViewHolder(
            PlaylistRecycleviewSmallItemBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = catalog.size

    override fun onBindViewHolder(holder: PlayerCatalogViewHolder, position: Int) {
        holder.bind(catalog[position])
        holder.itemView.setOnClickListener {
            addTrackToPlaylist(track, catalog[position].id)
        }
    }
}