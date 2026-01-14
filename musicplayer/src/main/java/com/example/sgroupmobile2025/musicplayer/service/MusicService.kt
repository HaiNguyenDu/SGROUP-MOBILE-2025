package com.example.sgroupmobile2025.musicplayer.service

import android.app.Service
import android.content.*
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.sgroupmobile2025.musicplayer.constants.MusicAction
import com.example.sgroupmobile2025.musicplayer.data.Track

class MusicService : Service() {

    private lateinit var player: MediaPlayer
    private var playlist: List<Track> = emptyList()
    private var currentIndex = -1
    private var isPlaying = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()

        val filter = IntentFilter().apply {
            addAction(MusicAction.ACTION_SET_PLAYLIST)
            addAction(MusicAction.ACTION_PLAY_BY_INDEX)
            addAction(MusicAction.ACTION_PLAY)
            addAction(MusicAction.ACTION_PAUSE)
            addAction(MusicAction.ACTION_NEXT)
            addAction(MusicAction.ACTION_PREV)
            addAction(MusicAction.ACTION_STOP)
        }

        LocalBroadcastManager
            .getInstance(this)
            .registerReceiver(actionReceiver, filter)

        Log.e("MUSIC_SERVICE", "Service created & receiver registered")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }


    private fun playByIndex(index: Int) {
        if (index < 0 || index >= playlist.size) return
        currentIndex = index
        val track = playlist[index]
        play(track.preview, track.title, track.artist.name)
    }

    private fun play(url: String, title: String, artist: String) {
        try {
            player.reset()
            player.setAudioStreamType(AudioManager.STREAM_MUSIC)
            player.setDataSource(url)
            player.prepare()
            player.start()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

        isPlaying = true

        player.setOnCompletionListener {
            next()
        }

        updateUI(title, artist)
    }

    private fun pause() {
        if (player.isPlaying) {
            player.pause()
            isPlaying = false
            updateUI()
        }
    }

    private fun resume() {
        if (!player.isPlaying) {
            player.start()
            isPlaying = true
            updateUI()
        }
    }

    private fun next() {
        if (playlist.isEmpty()) return
        val nextIndex = (currentIndex + 1) % playlist.size
        playByIndex(nextIndex)
    }

    private fun prev() {
        if (playlist.isEmpty()) return
        val prevIndex =
            if (currentIndex - 1 < 0) playlist.size - 1
            else currentIndex - 1
        playByIndex(prevIndex)
    }

    private fun stopMusic() {
        try { player.stop() } catch (_: Exception) {}
        try { player.release() } catch (_: Exception) {}
        isPlaying = false
        updateUI()
        stopSelf()
    }


    private val actionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            Log.e("MUSIC_SERVICE", "RECEIVED: ${intent?.action}")

            when (intent?.action) {

                MusicAction.ACTION_SET_PLAYLIST -> {
                    val list = intent.getParcelableArrayListExtra<Track>(
                        MusicAction.EXTRA_PLAYLIST
                    )
                    if (list != null) playlist = list
                }

                MusicAction.ACTION_PLAY_BY_INDEX -> {
                    val index = intent.getIntExtra(
                        MusicAction.EXTRA_INDEX, -1
                    )
                    playByIndex(index)
                }

                MusicAction.ACTION_PLAY -> resume()
                MusicAction.ACTION_PAUSE -> pause()
                MusicAction.ACTION_NEXT -> next()
                MusicAction.ACTION_PREV -> prev()
                MusicAction.ACTION_STOP -> stopMusic()
            }
        }
    }


    private fun updateUI(title: String? = null, artist: String? = null) {
        val intent = Intent(MusicAction.ACTION_UPDATE_UI).apply {
            putExtra(MusicAction.EXTRA_TITLE, title)
            putExtra(MusicAction.EXTRA_ARTIST, artist)
            putExtra(MusicAction.EXTRA_IS_PLAYING, isPlaying)
        }

        LocalBroadcastManager
            .getInstance(this)
            .sendBroadcast(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
        try { player.release() } catch (_: Exception) {}

        LocalBroadcastManager
            .getInstance(this)
            .unregisterReceiver(actionReceiver)
    }
}
