package com.example.sgroupmobile2025.ui.map

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.sgroupmobile2025.R
import com.example.sgroupmobile2025.common.constants.IntentKeys
import com.example.sgroupmobile2025.common.constants.IntentKeys.PLACE_ID
import com.example.sgroupmobile2025.databinding.ActivityMapDetailBinding
import com.example.sgroupmobile2025.viewmodel.DetailPlaceViewModel
import kotlinx.coroutines.launch

class MapDetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMapDetailBinding.inflate(layoutInflater)}
    private lateinit var detailPlaceViewModel: DetailPlaceViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        initView()
        handleViewCompat()
        handleObserve()
        handleToolbar()
    }
    fun initView(){
        val placeId = intent.getStringExtra(PLACE_ID)
        detailPlaceViewModel = DetailPlaceViewModel(application)
        if(placeId == null) return
        detailPlaceViewModel.initDetailPlace(placeId)
    }
    fun handleObserve(){
        lifecycleScope.launch {
            detailPlaceViewModel.detailPlace.collect { place ->
                binding.tvPlaceName.text = place?.address
                binding.lat.text = place?.lat.toString()
                binding.lng.text = place?.lng.toString()
            }
        }
        lifecycleScope.launch {
            detailPlaceViewModel.isLoading.collect { isLoading ->
                binding.lottieLoading.visibility =  if(isLoading) View.VISIBLE else View.GONE
                binding.overlay.visibility =  if(isLoading) View.VISIBLE else View.GONE
            }
        }
    }
    fun handleViewCompat(){
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun handleToolbar(){
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}