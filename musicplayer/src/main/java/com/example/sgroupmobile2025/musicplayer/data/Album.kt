package com.example.sgroupmobile2025.musicplayer.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Album(
    val id: Long,
    val title: String,
    @SerializedName("cover_medium")
    val coverMedium: String
): Parcelable
data class AlbumResponse(
    val data: List<Album>
)

data class AlbumDetailResponse(
    val id: Long,
    val title: String,

    @SerializedName("cover_medium")
    val coverMedium: String,

    val artist: Artist,

    val tracks: TrackResponse
)