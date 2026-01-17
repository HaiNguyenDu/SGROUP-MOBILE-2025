package com.example.sgroupmobile2025.musicplayer.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
    val id: Long,
    val title: String,
    val preview: String,
    val artist: Artist,
    val duration: Int,
    val album: Album
) : Parcelable

data class TrackResponse(
    val data: List<Track>
)
