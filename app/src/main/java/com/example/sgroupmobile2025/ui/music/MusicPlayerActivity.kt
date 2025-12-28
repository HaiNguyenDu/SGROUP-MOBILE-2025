package com.example.sgroupmobile2025.ui.music

import android.Manifest
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sgroupmobile2025.databinding.ActivityMusicPlayerBinding
import com.example.sgroupmobile2025.service.MusicService
import com.example.sgroupmobile2025.data.local.LocalData
import android.app.AlertDialog
import android.provider.Settings

class MusicPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMusicPlayerBinding
    private var musicService: MusicService? = null
    private var isBound = false
    private var audioList = mutableListOf<String>()
    private var currentAudioIndex = 0

    private val audioPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        when {
            granted -> {
                loadAudioList()
            }
            shouldShowRequestPermissionRationale(audioPermission) -> {
                showRationale()
            }
            else -> {
                showDialogGoToSetting()
            }
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.LocalBinder
            musicService = binder.getService()
            isBound = true
            Log.d("MusicPlayerActivity", "Service connected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    private val musicStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val state = intent?.getStringExtra("state")
            val position = intent?.getIntExtra("position", 0) ?: 0
            val duration = intent?.getIntExtra("duration", 0) ?: 0

            updateUI(state, position, duration)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bind service
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)

        // Register broadcast receiver
        val intentFilter = IntentFilter("MUSIC_STATE_CHANGED")
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(musicStateReceiver, intentFilter)

        // Kiểm tra permission
        val hasPermission = checkSelfPermission(audioPermission) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            loadAudioList()
        } else {
            permissionLauncher.launch(audioPermission)
        }

        setupUI()
    }

    private fun setupUI() {
        // Play button
        binding.btnPlay.setOnClickListener {
            if (audioList.isNotEmpty()) {
                val audioPath = audioList[currentAudioIndex]
                musicService?.playMusic(audioPath)
                binding.tvCurrentTrack.text = "Playing: ${getAudioFileName(audioPath)}"
            }
        }

        // Pause button
        binding.btnPause.setOnClickListener {
            musicService?.pauseMusic()
        }

        // Resume button
        binding.btnResume.setOnClickListener {
            musicService?.resumeMusic()
        }

        // Stop button
        binding.btnStop.setOnClickListener {
            musicService?.stopMusic()
        }

        // Previous button
        binding.btnPrevious.setOnClickListener {
            if (currentAudioIndex > 0) {
                currentAudioIndex--
                playAudio(audioList[currentAudioIndex])
            }
        }

        // Next button
        binding.btnNext.setOnClickListener {
            if (currentAudioIndex < audioList.size - 1) {
                currentAudioIndex++
                playAudio(audioList[currentAudioIndex])
            }
        }

        // SeekBar
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    musicService?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Volume
        binding.volumeSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                musicService?.setVolume(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // RecyclerView danh sách nhạc
        setupAudioListRecyclerView()

        // Back button
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setupAudioListRecyclerView() {
        binding.rvAudioList.layoutManager = LinearLayoutManager(this)
        updateAudioListAdapter()
    }

    private fun updateAudioListAdapter() {
        val adapter = AudioListAdapter(audioList, currentAudioIndex) { index ->
            currentAudioIndex = index
            playAudio(audioList[index])
        }
        binding.rvAudioList.adapter = adapter
    }

    private fun playAudio(audioPath: String) {
        musicService?.playMusic(audioPath)
        binding.tvCurrentTrack.text = "Playing: ${getAudioFileName(audioPath)}"
        updateAudioListAdapter()
    }

    private fun loadAudioList() {
        try {
            val localData = LocalData(this)
            audioList = localData.getAudioFiles().toMutableList()
            Log.e("MusicPlayerActivityyyyy", audioList.toString())


            if (audioList.isNotEmpty()) {
                binding.tvCurrentTrack.text = "Chọn bài hát (Tổng: ${audioList.size} bài)"
                updateAudioListAdapter()
            } else {
                binding.tvCurrentTrack.text = "Không tìm thấy audio"
            }
        } catch (e: Exception) {
            Log.e("MusicPlayerActivity", "Error loading audio: ${e.message}")
        }
    }

    private fun updateUI(state: String?, position: Int, duration: Int) {
        binding.tvCurrentTime.text = formatTime(position)
        binding.tvDuration.text = formatTime(duration)
        binding.seekBar.max = duration
        binding.seekBar.progress = position

        when (state) {
            "playing" -> {
                binding.btnPlay.isEnabled = false
                binding.btnPause.isEnabled = true
                binding.btnResume.isEnabled = false
            }
            "paused" -> {
                binding.btnPlay.isEnabled = true
                binding.btnPause.isEnabled = false
                binding.btnResume.isEnabled = true
            }
            "stopped" -> {
                binding.btnPlay.isEnabled = true
                binding.btnPause.isEnabled = false
                binding.btnResume.isEnabled = false
                binding.seekBar.progress = 0
            }
        }
    }

    private fun formatTime(milliseconds: Int): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / 1000) / 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun getAudioFileName(path: String): String {
        return path.substringAfterLast("/")
    }

    private fun showRationale() {
        AlertDialog.Builder(this)
            .setTitle("Cần quyền truy cập audio")
            .setMessage("Ứng dụng cần quyền này để phát nhạc từ thư viện.")
            .setPositiveButton("Đồng ý") { dialog, _ ->
                permissionLauncher.launch(audioPermission)
                dialog.dismiss()
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDialogGoToSetting() {
        AlertDialog.Builder(this)
            .setTitle("Quyền bị từ chối vĩnh viễn")
            .setMessage("Ứng dụng cần quyền truy cập audio. Vui lòng bật quyền trong Cài đặt.")
            .setPositiveButton("Đi tới Cài đặt") { dialog, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = android.net.Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(connection)
        }
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(musicStateReceiver)
    }
}