package com.example.sgroupmobile2025.musicplayer.service

import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
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

    private lateinit var player: MediaPlayer
    private var playlist: List<Track> = emptyList()
    private var currentIndex = -1
    private var isPlaying = false

    private var currentTitle: String? = null
    private var currentArtist: String? = null
    private var currentImage: String? = null

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
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(actionReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(actionReceiver, filter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try { player.release() } catch (_: Exception) {}
        unregisterReceiver(actionReceiver)
    }


    private fun buildEmptyNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_MUSIC_ID)
            .setSmallIcon(R.drawable.ic_music)
            .setContentTitle("Music Player")
            .setOngoing(true)
            .build()


    private fun playByIndex(index: Int) {
        if (index < 0 || index >= playlist.size) return
        currentIndex = index
        val track = playlist[index]
        play(track.preview, track.title, track.artist.name, track.album.coverMedium)
    }

    private fun play(url: String, title: String, artist: String, imageUrl: String?) {
        try {
            player.reset()
            player.setAudioStreamType(AudioManager.STREAM_MUSIC)
            player.setDataSource(url)
            player.prepare()
            player.start()

            currentTitle = title
            currentArtist = artist
            currentImage = imageUrl

            isPlaying = true
            showNotification(title, artist, imageUrl)
            updateUI(title, artist, imageUrl)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        player.setOnCompletionListener { next() }
    }

    private fun pause() {
        if (!player.isPlaying) return
        player.pause()
        isPlaying = false

        showNotification(currentTitle, currentArtist, currentImage)
        updateUI()
    }

    private fun resume() {
        if (player.isPlaying) return
        player.start()
        isPlaying = true

        showNotification(currentTitle, currentArtist, currentImage)
        updateUI()
    }

    private fun next() {
        if (playlist.isEmpty()) return
        playByIndex((currentIndex + 1) % playlist.size)
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
        isPlaying = false
        updateUI()
        stopSelf()
    }


    private val actionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

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


    private fun updateUI(
        title: String? = null,
        artist: String? = null,
        imageUrl: String? = null
    ) {
        val intent = Intent(MusicAction.ACTION_UPDATE_UI).apply {
            putExtra(MusicAction.EXTRA_TITLE, title)
            putExtra(MusicAction.EXTRA_ARTIST, artist)
            putExtra(MusicAction.EXTRA_IS_PLAYING, isPlaying)
            putExtra(MusicAction.EXTRA_IMAGE, imageUrl)
            setPackage(packageName)
        }
        sendBroadcast(intent)
    }


    private fun prevPendingIntent() =
        PendingIntent.getBroadcast(
            this, 1,
            Intent(MusicAction.ACTION_PREV).apply { setPackage(packageName) },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    private fun playPendingIntent(): PendingIntent {
        val action =
            if (isPlaying) MusicAction.ACTION_PAUSE
            else MusicAction.ACTION_PLAY

        return PendingIntent.getBroadcast(
            this, 2,
            Intent(action).apply { setPackage(packageName) },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun nextPendingIntent() =
        PendingIntent.getBroadcast(
            this, 3,
            Intent(MusicAction.ACTION_NEXT).apply { setPackage(packageName) },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    private fun showNotification(
        title: String?,
        artist: String?,
        imageUrl: String?
    ) {
        if (imageUrl.isNullOrEmpty()) {
            val defaultBitmap =
                BitmapFactory.decodeResource(resources, R.drawable.ic_music)
            buildAndShowNotification(title, artist, defaultBitmap)
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
        albumBitmap: Bitmap?
    ) {
        val notification = buildCustomNotification(title, artist, albumBitmap)
        startForeground(1, notification)
    }

    private fun buildCustomNotification(
        title: String?,
        artist: String?,
        albumBitmap: Bitmap?
    ): Notification {

        val smallRv = RemoteViews(packageName, R.layout.notification_music_small)
        val bigRv   = RemoteViews(packageName, R.layout.notification_music_custom)

        smallRv.setTextViewText(R.id.tvTitle, title ?: "Unknown")
        bigRv.setTextViewText(R.id.tvTitle, title ?: "Unknown")
        bigRv.setTextViewText(R.id.tvArtist, artist ?: "")

        if (albumBitmap != null) {
            smallRv.setImageViewBitmap(R.id.imgAlbum, albumBitmap)
            bigRv.setImageViewBitmap(R.id.imgAlbum, albumBitmap)
            bigRv.setImageViewBitmap(R.id.imgBackground, albumBitmap)
        }

        val playIcon =
            if (isPlaying) R.drawable.ic_pause
            else R.drawable.ic_play

        smallRv.setImageViewResource(R.id.btn_play_noti, playIcon)
        bigRv.setImageViewResource(R.id.btn_play_noti, playIcon)

        smallRv.setOnClickPendingIntent(R.id.btn_play_noti, playPendingIntent())

        bigRv.setOnClickPendingIntent(R.id.btn_prev_noti, prevPendingIntent())
        bigRv.setOnClickPendingIntent(R.id.btn_play_noti, playPendingIntent())
        bigRv.setOnClickPendingIntent(R.id.btn_next_noti, nextPendingIntent())

        val openAppIntent = Intent(this, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            this, 0, openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_MUSIC_ID)
            .setSmallIcon(R.drawable.ic_music)
            .setContentIntent(contentPendingIntent)

            .setCustomContentView(smallRv)
            .setCustomBigContentView(bigRv)

            .setShowWhen(false)
            .setOnlyAlertOnce(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(isPlaying)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_MUSIC_ID,
                CHANNEL_MUSIC,
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }
}
