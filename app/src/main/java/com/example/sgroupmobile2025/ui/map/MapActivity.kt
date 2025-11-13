package com.example.sgroupmobile2025.ui.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sgroupmobile2025.common.constants.IntentKeys.DESTINATION_PLACE_ID
import com.example.sgroupmobile2025.common.constants.IntentKeys.ORIGIN_PLACE_ID
import com.example.sgroupmobile2025.databinding.ActivityMapBinding
import com.example.sgroupmobile2025.viewmodel.GoongViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapActivity : AppCompatActivity() {

    private var isFrom = true
    private val _route = MutableStateFlow(Route())
    val route = _route.asStateFlow()

    private val binding by lazy { ActivityMapBinding.inflate(layoutInflater) }
    private lateinit var adapter: MapAdapter
    private lateinit var mapViewModel: GoongViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        handleViewCompat()
        initView()
        handleObserve()
        handleSearch()
        handleToolbar()
        handleClick()
    }
    fun handleClick(){
        binding.btnGo.setOnClickListener {
            val intent = Intent(this, MapDetailActivity::class.java)
            intent.putExtra(ORIGIN_PLACE_ID, route.value.originId)
            intent.putExtra(DESTINATION_PLACE_ID, route.value.destinationId)
            startActivity(intent)
        }
    }

    private fun handleSearch() {
        binding.etSeachMap.addTextChangedListener { editable ->
            val input = editable.toString()
            mapViewModel.getPlaces(input)
        }
    }

    private fun handleViewCompat() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleObserve() {
        lifecycleScope.launch {
            mapViewModel.responsePlaces.collect { response ->
                val places = response?.predictions.orEmpty()
                adapter.updatePlaces(places)
            }
        }

        lifecycleScope.launch {
            mapViewModel.isLoading.collect { isLoading ->
                val visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.lottieLoading.visibility = visibility
                binding.overlay.visibility = visibility
            }
        }

        lifecycleScope.launch {
            route.collect { route ->
                val hasOrigin = route.originId.isNotEmpty()
                val hasDestination = route.destinationId.isNotEmpty()
                binding.tvOrigin.visibility = if (hasOrigin) View.VISIBLE else View.GONE
                binding.tagDestination.visibility = if (hasOrigin) View.VISIBLE else View.GONE
                binding.tvDestination.visibility = if (hasDestination) View.VISIBLE else View.GONE
                binding.btnGo.visibility = if (hasDestination) View.VISIBLE else View.GONE
            }
        }
    }

    private fun initView() {
        mapViewModel = GoongViewModel(application)
        adapter = MapAdapter(emptyList()) { location, placeDescription ->
            if (isFrom) {
                binding.tvOrigin.text = placeDescription
                _route.value = _route.value.copy(originId = location)
            } else {
                binding.tvDestination.text = placeDescription
                _route.value = _route.value.copy(destinationId = location)
            }
            isFrom = !isFrom
        }

        binding.rcvMap.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvMap.adapter = adapter
    }

    private fun handleToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener { finish() }
    }
}

data class Route(
    val originId: String = "",
    val destinationId: String = ""
)
