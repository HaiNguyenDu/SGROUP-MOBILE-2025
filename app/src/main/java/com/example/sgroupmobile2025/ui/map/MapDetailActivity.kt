package com.example.sgroupmobile2025.ui.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.common.constants.IntentKeys.DESTINATION_PLACE_ID
import com.example.sgroupmobile2025.common.constants.IntentKeys.ORIGIN_PLACE_ID
import com.example.sgroupmobile2025.common.constants.MAP_KEY
import com.example.sgroupmobile2025.data.model.DetailPlace
import com.example.sgroupmobile2025.data.model.DirectionResponse
import com.example.sgroupmobile2025.databinding.ActivityMapDetailBinding
import com.example.sgroupmobile2025.viewmodel.DetailPlaceViewModel
import kotlinx.coroutines.launch
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.plugins.annotation.SymbolManager
import org.maplibre.android.plugins.annotation.SymbolOptions
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.PropertyFactory.*
import org.maplibre.android.style.sources.GeoJsonSource
import org.json.JSONObject
import org.maplibre.android.MapLibre
import org.maplibre.android.annotations.PolylineOptions
import org.maplibre.android.maps.MapLibreMap

class MapDetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMapDetailBinding.inflate(layoutInflater) }
    private lateinit var map: MapLibreMap
    private lateinit var mapView: MapView
    private val styleUrl = "https://tiles.goong.io/assets/goong_map_web.json?api_key=$MAP_KEY"
    private lateinit var detailPlaceViewModel: DetailPlaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        mapView = binding.mvPlace
        mapView.onCreate(savedInstanceState)

        initView()
        handleViewCompat()
        handleObserve()
        handleToolbar()
    }

    private fun initView() {
        val originId = intent.getStringExtra(ORIGIN_PLACE_ID)
        val destinationId = intent.getStringExtra(DESTINATION_PLACE_ID)
        detailPlaceViewModel = DetailPlaceViewModel(application)
        if (originId == null || destinationId == null) return
        detailPlaceViewModel.initDetailPlace(originId, destinationId)
    }

    private fun handleObserve() {
        lifecycleScope.launch {
            detailPlaceViewModel.detailOriginPlace.collect { origin ->
                val dest = detailPlaceViewModel.detailDestinationPlace.value
                if (origin != null && dest != null) {
                    createMap(origin, dest)
                    detailPlaceViewModel.getDirection(origin.toLocation(), dest.toLocation())
                }
            }
        }

        lifecycleScope.launch {
            detailPlaceViewModel.detailDestinationPlace.collect { dest ->
                val origin = detailPlaceViewModel.detailOriginPlace.value
                if (origin != null && dest != null) {
                    createMap(origin, dest)
                    detailPlaceViewModel.getDirection(origin.toLocation(), dest.toLocation())
                }
            }
        }

        lifecycleScope.launch {
            detailPlaceViewModel.direction.collect { direction ->
                if (direction != null) drawRoute(direction)
            }
        }

        lifecycleScope.launch {
            detailPlaceViewModel.isLoading.collect { loading ->
                binding.lottieLoading.visibility = if (loading) View.VISIBLE else View.GONE
                binding.overlay.visibility = if (loading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun createMap(originPlace: DetailPlace, destinationPlace: DetailPlace) {
        mapView.getMapAsync { map ->
            this.map = map
            map.setStyle(styleUrl) { style ->
                val symbolManager = SymbolManager(mapView, map, style)
                symbolManager.iconAllowOverlap = true

                style.addImage("marker-icon", vectorToBitmap(R.drawable.marker_icon, 80, 80))
                style.addImage("pin-marker-icon", vectorToBitmap(R.drawable.pin_map_marker, 80, 80))

                val origin = SymbolOptions()
                    .withLatLng(LatLng(originPlace.lat!!, originPlace.lng!!))
                    .withIconImage("pin-marker-icon")

                val destination = SymbolOptions()
                    .withLatLng(LatLng(destinationPlace.lat!!, destinationPlace.lng!!))
                    .withIconImage("marker-icon")

                symbolManager.create(origin)
                symbolManager.create(destination)

                val minLat = minOf(originPlace.lat!!, destinationPlace.lat!!)
                val maxLat = maxOf(originPlace.lat!!, destinationPlace.lat!!)
                val minLng = minOf(originPlace.lng!!, destinationPlace.lng!!)
                val maxLng = maxOf(originPlace.lng!!, destinationPlace.lng!!)

                val bounds = org.maplibre.android.geometry.LatLngBounds.Builder()
                    .include(LatLng(minLat, minLng))
                    .include(LatLng(maxLat, maxLng))
                    .build()

                map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
            }
        }
    }

    private fun drawRoute(direction: DirectionResponse) {
        if(!::map.isInitialized) return
        val route = direction.routes.firstOrNull() ?: return
        val polyPoints = decodePolyline(route.overviewPolyline.points)
        val lineOptions = PolylineOptions()
            .addAll(polyPoints)
            .width(6f)
            .color(getColor(R.color.light_pink))
        map.addPolyline(lineOptions)
    }

    private fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
            lng += dlng

            val latLng = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
            poly.add(latLng)
        }

        return poly
    }
    private fun vectorToBitmap(id: Int, width: Int, height: Int): Bitmap {
        val drawable = ResourcesCompat.getDrawable(resources, id, null)!!
        val bitmap = createBitmap(width, height)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun handleViewCompat() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun onStart() { super.onStart(); mapView.onStart() }
    override fun onResume() { super.onResume(); mapView.onResume() }
    override fun onPause() { super.onPause(); mapView.onPause() }
    override fun onStop() { super.onStop(); mapView.onStop() }
    override fun onDestroy() { super.onDestroy(); mapView.onDestroy() }
}
