package com.example.playlistmaker.ui.search


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.BatchingListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import clickDebounce
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackrecycleviewItemBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.use_cases.TrackSaveUseCase
import com.example.playlistmaker.ui.player.PlayerActivity

class AdapterSearch(val callBack: (Track) -> Unit) :
    RecyclerView.Adapter<ViewHolderOfSongs>() {

    var tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderOfSongs {
        val layoutInspector = LayoutInflater.from(parent.context)
        return ViewHolderOfSongs(
            TrackrecycleviewItemBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolderOfSongs, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                callBack(tracks[position])
                //  trackSaveUseCase.execute(tracks[position])

                val displayPlayer = Intent(it.context, PlayerActivity::class.java)
                displayPlayer.putExtra("track", tracks[position])
                it.context.startActivity(displayPlayer)
            }
        }
    }

    override fun getItemCount() = tracks.size

}

