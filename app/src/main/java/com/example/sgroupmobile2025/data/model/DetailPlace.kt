package com.example.sgroupmobile2025.data.model


data class DetailPlace(
    var id: String = "",
    var address: String? = null,
    var lat: Double? = null,
    var lng: Double? = null
){
    fun toLocation(): String{
        val location = this.lat.toString() + "," + this.lng.toString()
        return location
    }
}
