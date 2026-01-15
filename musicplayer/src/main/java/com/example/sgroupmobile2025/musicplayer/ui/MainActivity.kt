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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        viewCompat()
        initView()
        setupMiniPlayer()
    }

    // ================= MINI PLAYER =================

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
            startActivity(Intent(this, DetailActivity::class.java))
        }
    }

    // ================= SEND BROADCAST =================

    private fun sendAction(action: String) {
        val intent = Intent(action).apply {
            setPackage(packageName)   // 🔴 bắt buộc để Service nhận
        }
        sendBroadcast(intent)
    }

    // ================= RECEIVE UI UPDATE =================

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

            val title = intent?.getStringExtra(MusicAction.EXTRA_TITLE)
            val artist = intent?.getStringExtra(MusicAction.EXTRA_ARTIST)
            val image = intent?.getStringExtra(MusicAction.EXTRA_IMAGE)
            isPlaying = intent?.getBooleanExtra(
                MusicAction.EXTRA_IS_PLAYING,
                false
            ) ?: false

            if (!title.isNullOrEmpty()) {
                binding.miniPlayer.visibility = View.VISIBLE
                binding.tvSongName.text = title
                binding.tvArtist.text = artist ?: ""
            }
            if(!image.isNullOrEmpty()){
                Glide.with(binding.root)
                    .load(image)
                    .placeholder(R.drawable.ic_music)
                    .into(binding.imgSong)
            }

            // 🔴 đổi icon Play / Pause
            binding.btnPlay.setImageResource(
                if (isPlaying)
                    R.drawable.ic_pause
                else
                    R.drawable.ic_play
            )
        }
    }

    // ================= UI SETUP =================

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
