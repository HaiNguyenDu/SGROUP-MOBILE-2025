package com.example.sgroupmobile2025.musicplayer.service

import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.sgroupmobile2025.musicplayer.R
import com.example.sgroupmobile2025.musicplayer.constants.MusicAction
import com.example.sgroupmobile2025.musicplayer.constants.Notification.CHANNEL_MUSIC
import com.example.sgroupmobile2025.musicplayer.constants.Notification.CHANNEL_MUSIC_ID
import com.example.sgroupmobile2025.musicplayer.data.Track
import com.example.sgroupmobile2025.musicplayer.ui.MainActivity

class MusicService : Service() {

    private var player: MediaPlayer? = null
    private var playlist: List<Track> = emptyList()
    private var currentIndex = -1
    private var isPlaying = false

    private var currentTitle: String? = null
    private var currentArtist: String? = null
    private var currentImage: String? = null

    private val handler = Handler(Looper.getMainLooper())
    private var progressRunnable: Runnable? = null

    override fun onBind(intent: Intent?): IBinder? = null

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate() {
        super.onCreate()

        player = MediaPlayer()
        createNotificationChannel()
        startForeground(1, buildEmptyNotification())

        val filter = IntentFilter().apply {
            addAction(MusicAction.ACTION_SET_PLAYLIST)
            addAction(MusicAction.ACTION_PLAY_BY_INDEX)
            addAction(MusicAction.ACTION_PLAY)
            addAction(MusicAction.ACTION_PAUSE)
            addAction(MusicAction.ACTION_NEXT)
            addAction(MusicAction.ACTION_PREV)
            addAction(MusicAction.ACTION_STOP)
            addAction(MusicAction.ACTION_SEEK_TO)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(actionReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(actionReceiver, filter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        stopProgress()
        try {
            player?.release()
        } catch (_: Exception) {}

        player = null
        unregisterReceiver(actionReceiver)
    }


    private fun playByIndex(index: Int) {
        if (index !in playlist.indices) return
        currentIndex = index
        val track = playlist[index]
        play(track.preview, track.title, track.artist.name, track.album.coverMedium)
    }

    private fun play(url: String, title: String, artist: String, imageUrl: String?) {
        try {
            player?.reset()
            player?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            player?.setDataSource(url)
            player?.prepare()
            player?.start()

            currentTitle = title
            currentArtist = artist
            currentImage = imageUrl
            isPlaying = true

            startProgress()
            showNotification(title, artist, imageUrl)
            updateUI(title, artist, imageUrl)

            player?.setOnCompletionListener { next() }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pause() {
        if (player?.isPlaying != true) return
        player?.pause()
        isPlaying = false
        showNotification(currentTitle, currentArtist, currentImage)
        updateUI()
    }

    private fun resume() {
        if (player?.isPlaying == true) return
        player?.start()
        isPlaying = true
        showNotification(currentTitle, currentArtist, currentImage)
        updateUI()
    }

    private fun stopMusic() {
        stopProgress()

        try {
            player?.pause()
            player?.seekTo(0)
        } catch (_: Exception) {}

        isPlaying = false
        currentTitle = null
        currentArtist = null
        currentImage = null

        updateUI()

    }


    private fun next() {
        if (playlist.isEmpty()) return
        playByIndex((currentIndex + 1) % playlist.size)
    }

    private fun prev() {
        if (playlist.isEmpty()) return
        val index = if (currentIndex - 1 < 0) playlist.lastIndex else currentIndex - 1
        playByIndex(index)
    }


    private fun startProgress() {
        stopProgress()
        progressRunnable = object : Runnable {
            override fun run() {
                if (player != null && player!!.isPlaying) {
                    val intent = Intent(MusicAction.ACTION_UPDATE_UI).apply {
                        putExtra(MusicAction.EXTRA_POSITION, player!!.currentPosition)
                        putExtra(MusicAction.EXTRA_DURATION, player!!.duration)
                        putExtra(MusicAction.EXTRA_IS_PLAYING, isPlaying)
                        setPackage(packageName)
                    }
                    sendBroadcast(intent)
                    handler.postDelayed(this, 1000)
                }
            }
        }
        handler.post(progressRunnable!!)
    }

    private fun stopProgress() {
        progressRunnable?.let { handler.removeCallbacks(it) }
        progressRunnable = null
    }


    private val actionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {

                MusicAction.ACTION_SET_PLAYLIST -> {
                    intent.getParcelableArrayListExtra<Track>(
                        MusicAction.EXTRA_PLAYLIST
                    )?.let { playlist = it }
                }

                MusicAction.ACTION_PLAY_BY_INDEX ->
                    playByIndex(intent.getIntExtra(MusicAction.EXTRA_INDEX, -1))

                MusicAction.ACTION_SEEK_TO -> {
                    val pos = intent.getIntExtra(MusicAction.EXTRA_SEEK_POSITION, 0)
                    if (player != null && pos in 0..player!!.duration) {
                        player!!.seekTo(pos)
                    }
                }

                MusicAction.ACTION_PLAY -> resume()
                MusicAction.ACTION_PAUSE -> pause()
                MusicAction.ACTION_NEXT -> next()
                MusicAction.ACTION_PREV -> prev()
                MusicAction.ACTION_STOP -> stopMusic()
            }
        }
    }


    private fun updateUI(
        title: String? = null,
        artist: String? = null,
        imageUrl: String? = null
    ) {
        sendBroadcast(
            Intent(MusicAction.ACTION_UPDATE_UI).apply {
                putExtra(MusicAction.EXTRA_TITLE, title)
                putExtra(MusicAction.EXTRA_ARTIST, artist)
                putExtra(MusicAction.EXTRA_IS_PLAYING, isPlaying)
                putExtra(MusicAction.EXTRA_IMAGE, imageUrl)
                setPackage(packageName)
            }
        )
    }


    private fun buildEmptyNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_MUSIC_ID)
            .setSmallIcon(R.drawable.ic_music)
            .setContentTitle("Music Player")
            .setOngoing(true)
            .build()

    private fun showNotification(title: String?, artist: String?, imageUrl: String?) {
        if (imageUrl.isNullOrEmpty()) {
            buildAndShowNotification(
                title,
                artist,
                BitmapFactory.decodeResource(resources, R.drawable.ic_music)
            )
            return
        }

        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    buildAndShowNotification(title, artist, resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    private fun buildAndShowNotification(
        title: String?,
        artist: String?,
        bitmap: Bitmap?
    ) {
        startForeground(1, buildCustomNotification(title, artist, bitmap))
    }

    private fun buildCustomNotification(
        title: String?,
        artist: String?,
        albumBitmap: Bitmap?
    ): Notification {

        val small = RemoteViews(packageName, R.layout.notification_music_small)
        val big = RemoteViews(packageName, R.layout.notification_music_custom)

        small.setTextViewText(R.id.tvTitle, title ?: "Unknown")
        big.setTextViewText(R.id.tvTitle, title ?: "Unknown")
        big.setTextViewText(R.id.tvArtist, artist ?: "")

        albumBitmap?.let {
            small.setImageViewBitmap(R.id.imgAlbum, it)
            big.setImageViewBitmap(R.id.imgAlbum, it)
            big.setImageViewBitmap(R.id.imgBackground, it)
        }

        val playIcon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        small.setImageViewResource(R.id.btn_play_noti, playIcon)
        big.setImageViewResource(R.id.btn_play_noti, playIcon)

        small.setOnClickPendingIntent(R.id.btn_play_noti, playPendingIntent())
        big.setOnClickPendingIntent(R.id.btn_prev_noti, prevPendingIntent())
        big.setOnClickPendingIntent(R.id.btn_play_noti, playPendingIntent())
        big.setOnClickPendingIntent(R.id.btn_next_noti, nextPendingIntent())

        val openApp = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_MUSIC_ID)
            .setSmallIcon(R.drawable.ic_music)
            .setContentIntent(openApp)
            .setCustomContentView(small)
            .setCustomBigContentView(big)
            .setOnlyAlertOnce(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(isPlaying)
            .build()
    }

    private fun playPendingIntent() =
        PendingIntent.getBroadcast(
            this, 2,
            Intent(if (isPlaying) MusicAction.ACTION_PAUSE else MusicAction.ACTION_PLAY)
                .setPackage(packageName),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    private fun prevPendingIntent() =
        PendingIntent.getBroadcast(
            this, 1,
            Intent(MusicAction.ACTION_PREV).setPackage(packageName),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    private fun nextPendingIntent() =
        PendingIntent.getBroadcast(
            this, 3,
            Intent(MusicAction.ACTION_NEXT).setPackage(packageName),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(
                    NotificationChannel(
                        CHANNEL_MUSIC_ID,
                        CHANNEL_MUSIC,
                        NotificationManager.IMPORTANCE_LOW
                    )
                )
        }
    }
}
