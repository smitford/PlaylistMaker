package com.example.playlistmaker.trackRecycleView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class CustomRecyclerAdapter(private val tracks: List<Track>) :
    RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolderOfSongs>() {
    class ViewHolderOfSongs(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val albumImg: ImageView = itemView.findViewById(R.id.album_image)
        private val artistName: TextView = itemView.findViewById(R.id.artist_name)
        private val trackName: TextView = itemView.findViewById(R.id.song_name)
        private val trackTime: TextView = itemView.findViewById(R.id.song_length)

        fun bind(track: Track) {
            trackName.text = track.trackName
            trackTime.text = track.trackTimeMillis
            artistName.text = track.artistName
            Glide.with(albumImg)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(2))
                .into(albumImg)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderOfSongs {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.trackrecycleview_item, parent, false)
        return ViewHolderOfSongs(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderOfSongs, position: Int) {

        holder.bind(tracks[position])
    }

    override fun getItemCount() = tracks.size
}