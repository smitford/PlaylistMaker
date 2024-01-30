package com.example.playlistmaker.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackrecycleviewItemBinding
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.callbackFlow


class PlaylistAdapter(
    val callBackToPlaylist: (Track) -> Unit,
    val callBackDeleteTrack: (Int) -> Unit,
) :
    RecyclerView.Adapter<PlaylistViewHolder>() {
    var tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(
            TrackrecycleviewItemBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            callBackToPlaylist(tracks[position])
        }

        holder.itemView.setOnLongClickListener {
            callBackDeleteTrack(tracks[position].trackId)
            true
        }
    }
}