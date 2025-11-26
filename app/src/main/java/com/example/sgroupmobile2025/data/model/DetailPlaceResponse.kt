package com.example.sgroupmobile2025.data.model

import com.google.gson.annotations.SerializedName

data class DetailPlaceResponse(
    val result: PlaceResult,
    val status: String
){
    fun toDetailPlace(): DetailPlace{
        val detailPlace = DetailPlace(result.placeId, result.formattedAddress, result.geometry?.location?.lat, result.geometry?.location?.lng)
        return detailPlace
    }
}

data class PlaceResult(
    @SerializedName("place_id")
    val placeId: String,
    @SerializedName("formatted_address")
    val formattedAddress: String?,
    val geometry: Geometry?,
    @SerializedName("plus_code")
    val plusCode: PlusCode?, // dùng chung class PlusCode đã có trong AutoCompleteResponse
    val compound: Compound?,
    val name: String?,
    val url: String?,
    val types: List<String>?,
    @SerializedName("deprecated_description")
    val deprecatedDescription: String?,
    @SerializedName("deprecated_compound")
    val deprecatedCompound: DeprecatedCompound? // dùng chung DeprecatedCompound đã có
)

data class Geometry(
    val location: Location?
)

data class Location(
    val lat: Double?,
    val lng: Double?
)
