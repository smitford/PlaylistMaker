package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.trackRecycleView.Track
import com.example.playlistmaker.trackRecycleView.ViewHolderOfSongs

class AdapterSearch (private val tracks: List<Track>) :
    RecyclerView.Adapter<ViewHolderOfSongs>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderOfSongs {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.trackrecycleview_item, parent, false)
        return ViewHolderOfSongs(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderOfSongs, position: Int) {

        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {

            SearchHistory.saveHistory(tracks[position])

        }

    }

    override fun getItemCount() = tracks.size
}