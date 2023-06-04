package com.example.playlistmaker.trackRecycleView

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.*

data class Track(
    val trackName: String,
    val artistName: String,
    var trackTimeMillis: String,
    val artworkUrl100: String,
    val trackId: Int,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        trackName = parcel.readString().toString(),
        artistName = parcel.readString().toString(),
        trackTimeMillis = parcel.readString().toString(),
        artworkUrl100 = parcel.readString().toString(),
        trackId = parcel.readInt(),
        collectionName = parcel.readString().toString(),
        releaseDate = parcel.readString().toString(),
        primaryGenreName = parcel.readString().toString(),
        country = parcel.readString().toString(),

        ) {
    }

    fun changeFormat() {
        this.trackTimeMillis =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis.toLong())
    }

    override fun equals(other: Any?): Boolean =
        (other is Track) && this.trackId == other.trackId && other.trackId == this.trackId

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeString(trackTimeMillis)
        parcel.writeString(artworkUrl100)
        parcel.writeInt(trackId)
        parcel.writeString(collectionName)
        parcel.writeString(releaseDate)
        parcel.writeString(primaryGenreName)
        parcel.writeString(country)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }


}