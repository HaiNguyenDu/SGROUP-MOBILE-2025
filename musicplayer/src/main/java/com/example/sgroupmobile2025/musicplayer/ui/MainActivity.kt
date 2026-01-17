package com.example.sgroupmobile2025.musicplayer.ui

import android.annotation.SuppressLint
import android.content.*
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.sgroupmobile2025.musicplayer.R
import com.example.sgroupmobile2025.musicplayer.adapter.FragmentAdaper
import com.example.sgroupmobile2025.musicplayer.constants.Constants.FRAGMENT_FAVOURITE
import com.example.sgroupmobile2025.musicplayer.constants.Constants.FRAGMENT_HOME
import com.example.sgroupmobile2025.musicplayer.constants.MusicAction
import com.example.sgroupmobile2025.musicplayer.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var isPlaying = false
    private var currentTitle: String? = null
    private var currentArtist: String? = null
    private var currentImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        viewCompat()
        initView()
        setupMiniPlayer()
    }


    private fun setupMiniPlayer() {

        binding.miniPlayer.visibility = View.GONE

        binding.btnPlay.setOnClickListener {
            sendAction(
                if (isPlaying)
                    MusicAction.ACTION_PAUSE
                else
                    MusicAction.ACTION_PLAY
            )
        }

        binding.btnClose.setOnClickListener {
            sendAction(MusicAction.ACTION_STOP)
            binding.miniPlayer.visibility = View.GONE
        }

        binding.miniPlayer.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(MusicAction.EXTRA_TITLE, currentTitle)
                putExtra(MusicAction.EXTRA_ARTIST, currentArtist)
                putExtra(MusicAction.EXTRA_IMAGE, currentImage)
                putExtra(MusicAction.EXTRA_IS_PLAYING, isPlaying)
            }
            startActivity(intent)
        }
    }


    private fun sendAction(action: String) {
        val intent = Intent(action).apply {
            setPackage(packageName)
        }
        sendBroadcast(intent)
    }


    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(MusicAction.ACTION_UPDATE_UI)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(uiReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(uiReceiver, filter)
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(uiReceiver)
    }

    private val uiReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            currentTitle = intent?.getStringExtra(MusicAction.EXTRA_TITLE)
            currentArtist = intent?.getStringExtra(MusicAction.EXTRA_ARTIST)
            currentImage = intent?.getStringExtra(MusicAction.EXTRA_IMAGE)

            isPlaying = intent?.getBooleanExtra(
                MusicAction.EXTRA_IS_PLAYING,
                false
            ) ?: false

            if (!currentTitle.isNullOrEmpty()) {
                binding.miniPlayer.visibility = View.VISIBLE
                binding.tvSongName.text = currentTitle
                binding.tvArtist.text = currentArtist ?: ""
            }

            if (!currentImage.isNullOrEmpty()) {
                Glide.with(binding.root)
                    .load(currentImage)
                    .placeholder(R.drawable.ic_music)
                    .into(binding.imgSong)
            }

            binding.btnPlay.setImageResource(
                if (isPlaying)
                    R.drawable.ic_pause
                else
                    R.drawable.ic_play
            )
        }

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

    private fun initView() {
        binding.viewPager.adapter = FragmentAdaper(this)
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            when (position) {
                FRAGMENT_HOME -> {
                    tab.text = "Home"
                    tab.icon = getDrawable(R.drawable.ic_home)
                }

                FRAGMENT_FAVOURITE -> {
                    tab.text = "Favourite"
                    tab.icon = getDrawable(R.drawable.ic_love)
                }
            }
        }.attach()
    }
}
