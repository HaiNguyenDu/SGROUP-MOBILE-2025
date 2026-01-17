package com.example.sgroupmobile2025.musicplayer.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.sgroupmobile2025.musicplayer.R
import com.example.sgroupmobile2025.musicplayer.constants.MusicAction
import com.example.sgroupmobile2025.musicplayer.databinding.FragmentDetailBinding
import com.example.sgroupmobile2025.musicplayer.viewmodel.MusicViewModel
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MusicViewModel by activityViewModels()

    private var duration = 0
    private var isUserSeeking = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        setupControls()
        setupSeekBar()
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(MusicAction.ACTION_UPDATE_UI)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireContext().registerReceiver(
                uiReceiver,
                filter,
                Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            ContextCompat.registerReceiver(
                requireContext(),
                uiReceiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
    }

    override fun onStop() {
        super.onStop()
        requireContext().unregisterReceiver(uiReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.currentArtist.collect { artist ->
                        binding.tvArtist.text = artist
                    }
                }

                launch {
                    viewModel.currentTitle.collect { title ->
                        binding.tvTitle.text = title
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
                launch {
                    viewModel.currentImage.collect { image ->
                        if (image.isNotEmpty()) {
                            Glide.with(requireContext())
                                .load(image)
                                .placeholder(R.drawable.ic_music)
                                .into(binding.imgAlbum)
                        }
                    }
                }
            }
        }
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

            val isPlaying = intent?.getBooleanExtra(
                MusicAction.EXTRA_IS_PLAYING,
                false
            ) ?: false

            viewModel.updatePlayState(isPlaying)

            title?.let { viewModel.updateTitle(it) }
            artist?.let { viewModel.updateArtist(it) }

            if (!image.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(image)
                    .placeholder(R.drawable.ic_music)
                    .into(binding.imgAlbum)
            }
        }
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
            setPackage(requireContext().packageName)
        }
        requireContext().sendBroadcast(intent)
    }

    private fun setupControls() {

        binding.btnPlay.setOnClickListener {
            sendAction(
                if (viewModel.isPlay.value)
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
    }

    private fun sendAction(action: String) {
        val intent = Intent(action).apply {
            setPackage(requireContext().packageName)
        }
        requireContext().sendBroadcast(intent)
    }

    private fun updateTimeText(pos: Int, dur: Int) {
        binding.tvCurrentTime.text = formatTime(pos)
        binding.tvTotalTime.text = formatTime(dur)
    }

    private fun formatTime(ms: Int): String {
        val totalSec = ms / 1000
        val min = totalSec / 60
        val sec = totalSec % 60
        return String.format("%d:%02d", min, sec)
    }
}
