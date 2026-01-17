package com.example.sgroupmobile2025.musicplayer.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artist(
    val id: Long,
    val name: String,
    @SerializedName("picture_medium")
    val pictureMedium: String
): Parcelable

data class ArtistResponse(
    val data: List<Artist>
)


data class ArtistDetailResponse(
    val id: Long,
    val name: String,

    @SerializedName("picture_medium")
    val pictureMedium: String,

    @SerializedName("nb_fan")
    val nbFan: Int
)
