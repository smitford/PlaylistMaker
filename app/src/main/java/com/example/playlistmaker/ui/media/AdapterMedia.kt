package com.example.playlistmaker.ui.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import clickDebounce
import com.example.playlistmaker.databinding.TrackrecycleviewItemBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.ViewHolderOfSongs

class AdapterMedia(val onTrackClicked: (Track) -> Unit) :
    RecyclerView.Adapter<ViewHolderMedia>() {
    var tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMedia {
        val layoutInspector = LayoutInflater.from(parent.context)
        return ViewHolderMedia(
            TrackrecycleviewItemBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolderMedia, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                onTrackClicked(tracks[position])
            }
        }
    }

    override fun getItemCount() = tracks.size
}