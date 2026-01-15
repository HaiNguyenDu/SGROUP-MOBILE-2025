package com.example.sgroupmobile2025.musicplayer.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sgroupmobile2025.musicplayer.constants.MusicAction
import com.example.sgroupmobile2025.musicplayer.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupControls()
    }

    private fun setupControls() {

        binding.btnPlay.setOnClickListener {
            sendAction(MusicAction.ACTION_PLAY)
        }

        binding.btnPrev.setOnClickListener {
            sendAction(MusicAction.ACTION_PREV)
        }

        binding.btnNext.setOnClickListener {
            sendAction(MusicAction.ACTION_NEXT)
        }
    }

    private fun sendAction(action: String) {
        val intent = Intent(action)
        sendBroadcast(intent)
    }
}
