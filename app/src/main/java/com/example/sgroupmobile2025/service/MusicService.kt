package com.example.sgroupmobile2025.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MusicService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val binder = LocalBinder()
    private var currentTrackPath: String = ""
    private var isPlaying = false

    inner class LocalBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("MusicService", "Service Created")
        mediaPlayer = MediaPlayer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MusicService", "Service Started")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MusicService", "Service Destroyed")
        stopMusic()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun playMusic(filePath: String) {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }

            mediaPlayer?.reset()
            mediaPlayer?.setDataSource(filePath)
            mediaPlayer?.prepareAsync()

            mediaPlayer?.setOnPreparedListener {
                mediaPlayer?.start()
                isPlaying = true
                currentTrackPath = filePath
                broadcastMusicState("playing")
                Log.d("MusicService", "Playing: $filePath")
            }

            mediaPlayer?.setOnCompletionListener {
                isPlaying = false
                broadcastMusicState("completed")
                Log.d("MusicService", "Music completed")
            }

            mediaPlayer?.setOnErrorListener { mp, what, extra ->
                Log.e("MusicService", "Error: $what, $extra")
                broadcastMusicState("error")
                false
            }

        } catch (e: Exception) {
            Log.e("MusicService", "Error playing music: ${e.message}")
        }
    }

    fun pauseMusic() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isPlaying = false
            broadcastMusicState("paused")
            Log.d("MusicService", "Music paused")
        }
    }

    fun resumeMusic() {
        if (mediaPlayer?.isPlaying == false && currentTrackPath.isNotEmpty()) {
            mediaPlayer?.start()
            isPlaying = true
            broadcastMusicState("playing")
            Log.d("MusicService", "Music resumed")
        }
    }

    fun stopMusic() {
        mediaPlayer?.stop()
        isPlaying = false
        currentTrackPath = ""
        broadcastMusicState("stopped")
        Log.d("MusicService", "Music stopped")
    }

    fun isPlaying(): Boolean {
        return isPlaying
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun getCurrentTrack(): String {
        return currentTrackPath
    }

    fun setVolume(volume: Int) {
        val vol = (volume / 100f)
        mediaPlayer?.setVolume(vol, vol)
    }

    private fun broadcastMusicState(state: String) {
        val intent = Intent("MUSIC_STATE_CHANGED")
        intent.putExtra("state", state)
        intent.putExtra("position", getCurrentPosition())
        intent.putExtra("duration", getDuration())
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}