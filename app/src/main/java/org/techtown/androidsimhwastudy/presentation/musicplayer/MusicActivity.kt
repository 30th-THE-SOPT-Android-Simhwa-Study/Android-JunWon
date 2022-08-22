package org.techtown.androidsimhwastudy.presentation.musicplayer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.techtown.androidsimhwastudy.databinding.ActivityMusicBinding

class MusicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMusicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickEvent()
    }

    private fun clickEvent() {
        binding.btnStart.setOnClickListener {
            val intent = Intent(this, MusicPlayerService::class.java)
            intent.action = Actions.START_FOREGROUND
            startService(intent)
        }

        binding.btnStop.setOnClickListener {
            val intent = Intent(this, MusicPlayerService::class.java)
            intent.action = Actions.STOP_FOREGROUND
            startService(intent)
        }
    }
}
