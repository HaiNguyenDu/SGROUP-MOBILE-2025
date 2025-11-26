package com.example.sgroupmobile2025.data.model

import com.google.gson.annotations.SerializedName

data class DirectionResponse(
    @SerializedName("geocoded_waypoints")
    val geocodedWaypoints: List<GeocodedWaypoint> = emptyList(),
    val routes: List<Route> = emptyList()
)

data class GeocodedWaypoint(
    val geocoder_status: String? = null,
    @SerializedName("place_id")
    val placeId: String? = null,
    val types: List<String>? = null
)

data class Route(
    val bounds: Bounds = Bounds(),
    val legs: List<Leg> = emptyList(),
    @SerializedName("overview_polyline")
    val overviewPolyline: Polyline = Polyline(),
    val warnings: List<String> = emptyList(),
    @SerializedName("waypoint_order")
    val waypointOrder: List<Int> = emptyList()
)

data class Bounds(
    val northeast: LatLng? = null,
    val southwest: LatLng? = null
)

data class LatLng(
    val lat: Double = 0.0,
    val lng: Double = 0.0
)

data class Leg(
    val distance: Distance = Distance(),
    val duration: Duration = Duration(),
    val steps: List<Step> = emptyList()
)

data class Distance(
    val text: String = "",
    val value: Int = 0
)

data class Duration(
    val text: String = "",
    val value: Int = 0
)

data class Step(
    val distance: Distance? = null,
    val duration: Duration? = null,
    @SerializedName("start_location")
    val startLocation: LatLng? = null,
    @SerializedName("end_location")
    val endLocation: LatLng? = null,
    val html_instructions: String? = null,
    @SerializedName("polyline")
    val polyline: Polyline? = null
)

data class Polyline(
    val points: String = ""
)
