package com.team.musicex

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat

class ForegroundService : Service() {
    private var musicName: String = ""

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        musicName = intent?.getStringExtra(MUSIC_TITLE) ?: ""
        createNotificationChannel()
        startForeground(1, createNotification())
        return START_STICKY
    }

    private fun createNotification(): Notification {

        val prevMusicIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(
            MainActivity.FLAG_PREVIOUS_BUTTON
        )
        val prevMusicPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            prevMusicIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val playMusicIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(
            MainActivity.FLAG_PLAY_BUTTON
        )
        val playMusicPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            playMusicIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val nextMusicIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(
            MainActivity.FLAG_NEXT_BUTTON
        )
        val nextMusicPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            nextMusicIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(musicName)
            .setContentText(MUSIC_DESCRIPTION)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.img_music))
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(MediaSessionCompat(baseContext, MUSIC_TAG).sessionToken)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.previous_btn, BTN_TITLE_PREVIOUS, prevMusicPendingIntent)
            .addAction(R.drawable.play_btn, BTN_TITLE_PLAY, playMusicPendingIntent)
            .addAction(R.drawable.next_btn, BTN_TITLE_NEXT, nextMusicPendingIntent)

        return notification.build()
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {

        const val CHANNEL_ID = "running_channel"
        const val CHANNEL_NAME = "channel"
        const val MUSIC_TITLE = "music_title"

        private const val MUSIC_DESCRIPTION = "Description Music"
        private const val MUSIC_TAG = "Music Tag"

        private const val BTN_TITLE_PREVIOUS = "Previous"
        private const val BTN_TITLE_PLAY = "Play"
        private const val BTN_TITLE_NEXT = "Next"

        fun newIntent(context: Context, musicTitle: String): Intent {
            return Intent(context, ForegroundService::class.java).apply {
                putExtra(MUSIC_TITLE, musicTitle)
            }
        }
    }
}