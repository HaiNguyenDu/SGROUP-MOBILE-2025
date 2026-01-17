package com.example.sgroupmobile2025.musicplayer.ui

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.sgroupmobile2025.musicplayer.R
import com.example.sgroupmobile2025.musicplayer.adapter.FragmentAdaper
import com.example.sgroupmobile2025.musicplayer.constants.Constants.FRAGMENT_DETAIL
import com.example.sgroupmobile2025.musicplayer.constants.Constants.FRAGMENT_HOME
import com.example.sgroupmobile2025.musicplayer.constants.MusicAction
import com.example.sgroupmobile2025.musicplayer.databinding.ActivityMainBinding
import com.example.sgroupmobile2025.musicplayer.viewmodel.MusicViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MusicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        viewCompat()
        initView()
        setupMiniPlayer()
        observeMiniPlayer()
        observePageChange()
    }

    private fun initView() {
        binding.viewPager.adapter = FragmentAdaper(this)
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            when (position) {
                FRAGMENT_HOME -> tab.icon = getDrawable(R.drawable.ic_home)
                FRAGMENT_DETAIL -> tab.icon = getDrawable(R.drawable.ic_play)
            }
        }.attach()
    }

    private fun observePageChange() {
        binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    updateMiniPlayerVisibility()
                }
            }
        )
    }

    private fun setupMiniPlayer() {
        binding.miniPlayer.visibility = View.GONE

        binding.btnPlay.setOnClickListener {
            sendAction(
                if (viewModel.isPlay.value)
                    MusicAction.ACTION_PAUSE
                else
                    MusicAction.ACTION_PLAY
            )
        }

        binding.btnClose.setOnClickListener {
            sendAction(MusicAction.ACTION_STOP)
            updateMiniPlayerVisibility(forceHide = true)
        }

        binding.miniPlayer.setOnClickListener {
            binding.viewPager.currentItem = FRAGMENT_DETAIL
        }
    }

    private fun updateMiniPlayerVisibility(forceHide: Boolean = false) {
        val hasMusic = viewModel.currentTitle.value.isNotEmpty()
        val isDetail = binding.viewPager.currentItem == FRAGMENT_DETAIL

        binding.miniPlayer.visibility =
            if (!forceHide && hasMusic && !isDetail)
                View.VISIBLE
            else
                View.GONE
    }

    private fun observeMiniPlayer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.currentTitle.collect { title ->
                        if (title.isNotEmpty()) {
                            binding.tvSongName.text = title
                            updateMiniPlayerVisibility()
                        }
                    }
                }

                launch {
                    viewModel.currentArtist.collect { artist ->
                        binding.tvArtist.text = artist
                    }
                }

                launch {
                    viewModel.currentImage.collect { image ->
                        if (image.isNotEmpty()) {
                            Glide.with(binding.root)
                                .load(image)
                                .placeholder(R.drawable.ic_music)
                                .into(binding.imgSong)
                        }
                    }
                }

                launch {
                    viewModel.isPlay.collect { isPlaying ->
                        binding.btnPlay.setImageResource(
                            if (isPlaying)
                                R.drawable.ic_pause
                            else
                                R.drawable.ic_play
                        )
                    }
                }
            }
        }
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

            intent?.getStringExtra(MusicAction.EXTRA_TITLE)
                ?.let { viewModel.updateTitle(it) }

            intent?.getStringExtra(MusicAction.EXTRA_ARTIST)
                ?.let { viewModel.updateArtist(it) }

            intent?.getStringExtra(MusicAction.EXTRA_IMAGE)
                ?.let { viewModel.updateImage(it) }

            val isPlaying = intent?.getBooleanExtra(
                MusicAction.EXTRA_IS_PLAYING,
                false
            ) ?: false

            viewModel.updatePlayState(isPlaying)
        }
    }

    private fun sendAction(action: String) {
        val intent = Intent(action).apply {
            setPackage(packageName)
        }
        sendBroadcast(intent)
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
}
