package com.example.sgroupmobile2025.musicplayer.ui

import android.annotation.SuppressLint
import android.content.*
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.sgroupmobile2025.musicplayer.R
import com.example.sgroupmobile2025.musicplayer.constants.MusicAction
import com.example.sgroupmobile2025.musicplayer.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var isPlaying = false
    private var duration = 0
    private var isUserSeeking = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        binding.imgAlbum.setImageResource(R.drawable.album)
        setContentView(binding.root)
        viewCompat()
        receiveDataFromIntent()
        setupControls()
        setupSeekBar()

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


    private fun receiveDataFromIntent() {

        val title = intent.getStringExtra(MusicAction.EXTRA_TITLE)
        val artist = intent.getStringExtra(MusicAction.EXTRA_ARTIST)
        val image = intent.getStringExtra(MusicAction.EXTRA_IMAGE)

        isPlaying = intent.getBooleanExtra(
            MusicAction.EXTRA_IS_PLAYING,
            false
        )

        binding.tvTitle.text = title ?: ""
        binding.tvArtist.text = artist ?: ""

        if (!image.isNullOrEmpty()) {
            Glide.with(this)
                .load(image)
                .placeholder(R.drawable.ic_music)
                .into(binding.imgAlbum)
        }

        updatePlayIcon()
    }
    private fun setupSeekBar() {

        binding.seekBar.setOnSeekBarChangeListener(
            object : android.widget.SeekBar.OnSeekBarChangeListener {

                override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {
                    isUserSeeking = true
                }

                override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {
                    isUserSeeking = false

                    val position = seekBar?.progress ?: 0
                    sendSeekTo(position)
                }

                override fun onProgressChanged(
                    seekBar: android.widget.SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {}
            }
        )
    }
    private fun sendSeekTo(position: Int) {
        val intent = Intent(MusicAction.ACTION_SEEK_TO).apply {
            putExtra(MusicAction.EXTRA_SEEK_POSITION, position)
            setPackage(packageName)
        }
        sendBroadcast(intent)
    }



    private fun setupControls() {

        binding.btnPlay.setOnClickListener {
            sendAction(
                if (isPlaying)
                    MusicAction.ACTION_PAUSE
                else
                    MusicAction.ACTION_PLAY
            )
        }

        binding.btnPrev.setOnClickListener {
            sendAction(MusicAction.ACTION_PREV)
        }

        binding.btnNext.setOnClickListener {
            sendAction(MusicAction.ACTION_NEXT)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }


    private fun sendAction(action: String) {
        val intent = Intent(action).apply {
            setPackage(packageName)   // 🔴 QUAN TRỌNG để Service nhận
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
            val pos = intent?.getIntExtra(
                MusicAction.EXTRA_POSITION, -1
            ) ?: -1

            val dur = intent?.getIntExtra(
                MusicAction.EXTRA_DURATION, -1
            ) ?: -1

            if (dur > 0) {
                duration = dur
                binding.seekBar.max = dur
            }

            if (pos >= 0 && !isUserSeeking) {
                binding.seekBar.progress = pos
                updateTimeText(pos, duration)
            }

            val title = intent?.getStringExtra(MusicAction.EXTRA_TITLE)
            val artist = intent?.getStringExtra(MusicAction.EXTRA_ARTIST)
            val image = intent?.getStringExtra(MusicAction.EXTRA_IMAGE)

            isPlaying = intent?.getBooleanExtra(
                MusicAction.EXTRA_IS_PLAYING,
                false
            ) ?: false

            if (!title.isNullOrEmpty()) {
                binding.tvTitle.text = title
                binding.tvArtist.text = artist ?: ""
            }

            if (!image.isNullOrEmpty()) {
                Glide.with(this@DetailActivity)
                    .load(image)
                    .placeholder(R.drawable.ic_music)
                    .into(binding.imgAlbum)
            }

            updatePlayIcon()
        }
    }
    private fun updateTimeText(pos: Int, dur: Int) {
        binding.tvTime.text =
            "${formatTime(pos)} / ${formatTime(dur)}"
    }

    private fun formatTime(ms: Int): String {
        val totalSec = ms / 1000
        val min = totalSec / 60
        val sec = totalSec % 60
        return String.format("%d:%02d", min, sec)
    }


    private fun updatePlayIcon() {
        binding.btnPlay.setImageResource(
            if (isPlaying)
                R.drawable.ic_pause
            else
                R.drawable.ic_play
        )
    }
}
