package com.example.playlistmaker.trackrecycleview


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


class ViewHolderOfSongs(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val albumImg: ImageView = itemView.findViewById(R.id.album_image)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackName: TextView = itemView.findViewById(R.id.song_name)
    private val trackTime: TextView = itemView.findViewById(R.id.length)

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

