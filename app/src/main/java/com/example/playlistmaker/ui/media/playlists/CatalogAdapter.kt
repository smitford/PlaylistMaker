package com.example.playlistmaker.ui.media.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistrecycleviewItemBinding
import com.example.playlistmaker.domain.models.PlaylistInfo

class CatalogAdapter() : RecyclerView.Adapter<CatalogViewHolder>() {

    var catalog = mutableListOf<PlaylistInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return CatalogViewHolder(
            PlaylistrecycleviewItemBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = catalog.size

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        holder.bind(catalog[position])
    }
}