package com.team.musicex

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.team.musicex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MainActivity.context = this

        getAllMusicInfo()
        mediaPlayer = MediaPlayer.create(this, mapMusicInfo.values.first())
        binding.tvTitleMusic.text = formatterTitleMusic(mapMusicInfo.keys.first())
        musicTitleService = formatterTitleMusic(mapMusicInfo.keys.first())
        checkVisibleButtons()
        initSeekBar()
        onClickButtonPlay()
        trackingMovementSeekBar()
        startedForegroundService()

        binding.btnNext.setOnClickListener {
            mediaPlayer.stop()
            clickButtonNextMusic()
            startedForegroundService()
        }
        binding.btnPrevious.setOnClickListener {
            mediaPlayer.stop()
            clickButtonPreviousMusic()
            startedForegroundService()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (isPlayMusic != null) {
            if (isPlayMusic as Boolean) {
                binding.btnPlay.setImageResource(R.drawable.pause_btn)
            } else {
                binding.btnPlay.setImageResource(R.drawable.play_btn)
            }
        }
        super.onWindowFocusChanged(hasFocus)
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        stopService(
            ForegroundService.newIntent(this, musicTitleService)
        )
        super.onDestroy()
    }

    companion object {
        lateinit var mediaPlayer: MediaPlayer
        lateinit var binding: ActivityMainBinding
        var musicTitleService: String = ""
        var isPlayMusic: Boolean? = null
        private var indexMusic = 0
        private lateinit var context: Context
        val mapMusicInfo: MutableMap<String, Int> = mutableMapOf()
        const val FLAG_PREVIOUS_BUTTON = "Previous"
        const val FLAG_PLAY_BUTTON = "Play"
        const val FLAG_NEXT_BUTTON = "Next"


        fun onClickButtonPlay() {
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

        fun initSeekBar() {
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

        fun trackingMovementSeekBar() {
            binding.seekBarMusic.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
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

        fun getAllMusicInfo() {
            val fields = R.raw::class.java.fields
            for (index in fields.indices) {
                val rid = fields[index].getInt(fields[index])
                mapMusicInfo[fields[index].name] = rid
            }
        }

        fun formatterTitleMusic(title: String): String {
            return title.replace("_", " ")
        }

        fun clickButtonNextMusic() {
            indexMusic += 1
            switchingMusic(indexMusic)
        }

        fun clickButtonPreviousMusic() {
            indexMusic -= 1
            switchingMusic(indexMusic)
        }

        fun switchingMusic(newIndex: Int) {
            checkVisibleButtons()
            for (currentIndex in mapMusicInfo.keys.indices) {
                if (currentIndex == newIndex) {
                    val newIdMusic = mapMusicInfo.values.toList()[newIndex]
                    val newTitleMusic = mapMusicInfo.keys.toList()[newIndex]
                    mediaPlayer = MediaPlayer.create(context, newIdMusic)
                    binding.btnPlay.setImageResource(R.drawable.pause_btn)
                    mediaPlayer.start()
                    binding.tvTitleMusic.text = formatterTitleMusic(newTitleMusic)
                    musicTitleService = formatterTitleMusic(newTitleMusic)
                }
            }
        }

        fun checkVisibleButtons() {
            binding.btnPrevious.isVisible = indexMusic != 0
            binding.btnNext.isVisible = indexMusic != mapMusicInfo.size - 1
        }

        fun startedForegroundService() {
            ContextCompat.startForegroundService(
                context,
                ForegroundService.newIntent(context, musicTitleService)
            )
        }
    }
}
