package com.example.sgroupmobile2025.ui.map

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sgroupmobile2025.databinding.ActivityMapBinding
import com.example.sgroupmobile2025.viewmodel.GoongViewModel
import kotlinx.coroutines.launch

class MapActivity : AppCompatActivity() {
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
    }
    fun handleSearch(){
        binding.etSeachMap.addTextChangedListener { editable ->
            val input = editable.toString()
            mapViewModel.getPlaces(input)
        }
    }
    fun handleViewCompat(){
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun handleObserve(){
        lifecycleScope.launch {
            mapViewModel.responsePlaces.collect {
                response ->
                if(response != null){
                    adapter.updatePlaces(response.predictions)
                }
            }
        }
    }
    fun initView(){
        mapViewModel = GoongViewModel(application)
        adapter = MapAdapter(emptyList())
        binding.rcvMap.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvMap.adapter = adapter
    }
}