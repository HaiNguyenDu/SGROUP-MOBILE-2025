package com.example.sgroupmobile2025.musicplayer.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.sgroupmobile2025.musicplayer.adapter.TrackListAdapter
import com.example.sgroupmobile2025.musicplayer.databinding.ActivityMainDetailListBinding
import com.example.sgroupmobile2025.musicplayer.viewmodel.MusicViewModel
import kotlinx.coroutines.launch
import kotlin.toString

class MainDetailList : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainDetailListBinding.inflate(layoutInflater)
    }

    private val viewModel: MusicViewModel by viewModels()
    private lateinit var adapter: TrackListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        viewCompat()
        setupHeader()
        setupRecycler()
        observeData()
        loadData()
    }
    private fun viewCompat() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    private fun setupHeader() {

        binding.btnBack.setOnClickListener { finish() }

        val title = intent.getStringExtra("TITLE") ?: ""
        val sub = intent.getStringExtra("SUB") ?: ""
        val image = intent.getStringExtra("IMAGE")

        binding.tvTitle.text = title
        binding.tvSub.text = sub

        if (!image.isNullOrEmpty()) {
            Glide.with(this)
                .load(image)
                .into(binding.imgHeader)
        }

    }

    private fun setupRecycler() {
        adapter = TrackListAdapter { track ->
        }

        binding.rvSongs.layoutManager = LinearLayoutManager(this)
        binding.rvSongs.adapter = adapter
    }

    private fun loadData() {
        val type = intent.getStringExtra("TYPE")
        val id = intent.getLongExtra("ID", -1)

        when (type) {
            "ALBUM" -> viewModel.loadTracksByAlbum(id)
            "ARTIST" -> viewModel.loadTracksByArtist(id)
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.detailTracks.collect {
                adapter.submitList(it)
                Log.e("dataaaaaanhac", it.toString())
            }
        }
    }
}
