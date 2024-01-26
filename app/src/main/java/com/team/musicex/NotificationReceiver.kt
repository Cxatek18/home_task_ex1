package com.team.musicex

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            MainActivity.FLAG_PREVIOUS_BUTTON -> {
                MainActivity.mediaPlayer.stop()
                MainActivity.clickButtonPreviousMusic()
                MainActivity.startedForegroundService()
            }

            MainActivity.FLAG_PLAY_BUTTON -> {
                if (!MainActivity.mediaPlayer.isPlaying) {
                    MainActivity.mediaPlayer.start()
                } else {
                    MainActivity.mediaPlayer.pause()
                }
            }

            MainActivity.FLAG_NEXT_BUTTON -> {
                MainActivity.mediaPlayer.stop()
                MainActivity.clickButtonNextMusic()
                MainActivity.startedForegroundService()
            }
        }
    }
}