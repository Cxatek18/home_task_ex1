package com.team.musicex

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.team.musicex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var binding: ActivityMainBinding
    private val mapMusicInfo: MutableMap<String, Int> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAllMusicInfo()
        mediaPlayer = MediaPlayer.create(this, mapMusicInfo.values.first())
        binding.tvTitleMusic.text = formatterTitleMusic(mapMusicInfo.keys.first())
        initSeekBar()
        onClickButtonPlay()
        trackingMovementSeekBar()
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
            } else {
                mediaPlayer.pause()
                binding.btnPlay.setImageResource(R.drawable.play_btn)
            }
        }
    }

    private fun initSeekBar() {
        binding.seekBarMusic.max = mediaPlayer.duration
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    binding.seekBarMusic.progress = mediaPlayer.currentPosition
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    binding.seekBarMusic.progress = 0
                }

            }
        }, 0)
    }

    private fun trackingMovementSeekBar() {
        binding.seekBarMusic.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun getAllMusicInfo() {
        val fields = R.raw::class.java.fields
        for (index in fields.indices) {
            val rid = fields[index].getInt(fields[index])
            mapMusicInfo[fields[index].name] = rid
        }
    }

    private fun formatterTitleMusic(title: String): String {
        return title.replace("_", " ")
    }
}
