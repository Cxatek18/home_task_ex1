package com.team.musicex

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.team.musicex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mediaPlayer = MediaPlayer.create(this, R.raw.cold_play_viva_la_diva)
        onClickButtonPlay()
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        super.onDestroy()
    }

    private fun onClickButtonPlay() {
        binding.btnPlay.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                binding.btnPlay.setImageResource(R.drawable.pause_btn)
                binding.seekBarMusic.progress = mediaPlayer.currentPosition
            } else {
                mediaPlayer.pause()
                binding.btnPlay.setImageResource(R.drawable.play_btn)
            }
        }
    }
}