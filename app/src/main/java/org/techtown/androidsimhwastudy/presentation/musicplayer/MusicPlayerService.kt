package org.techtown.androidsimhwastudy.presentation.musicplayer

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import org.techtown.androidsimhwastudy.R
import org.techtown.androidsimhwastudy.presentation.musicplayer.MusicNotification.NOTIFICATION_ID
import timber.log.Timber

class MusicPlayerService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var musicPlayList: List<Int> = listOf(R.raw.first, R.raw.second, R.raw.third)
    private var musicTitleList: List<String> = listOf("One", "Two", "Three")
    private var currentMusicIndex = 0
    private var currentMusic = R.raw.first
    private var currentMusicTitle = "One"

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, currentMusic).apply { isLooping = false }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // intent가 시스템에 의해 재생성되었을때 null값이므로 이 부분도 고려하여 처리
        when (intent?.action) {
            Actions.START_FOREGROUND -> {
                Timber.d("Start Foreground")
                startForegroundService(NotificationManager.IMPORTANCE_DEFAULT)
            }
            Actions.STOP_FOREGROUND -> {
                Timber.d("Stop Foreground")
                stopForegroundService()
            }
            Actions.PREV -> {
                Timber.d("Action PREV")
                mediaPlayer?.let { mediaPlayer ->
                    if (mediaPlayer.isPlaying) mediaPlayer.stop()
                }
                currentMusicIndex = (currentMusicIndex - 1 + 3) % 3
                playCurrentMusic()
            }
            Actions.PLAY -> {
                mediaPlayer?.let { mediaPlayer ->
                    if (mediaPlayer.isPlaying) {
                        Timber.d("Action PAUSE")
                        mediaPlayer.pause()
                    } else {
                        Timber.d("Action PLAY")
                        mediaPlayer.start()
                    }
                }
            }
            Actions.NEXT -> {
                Timber.d("Action NEXT")
                mediaPlayer?.let { mediaPlayer ->
                    if (mediaPlayer.isPlaying) mediaPlayer.stop()
                }
                currentMusicIndex = (currentMusicIndex + 1 + 3) % 3
                playCurrentMusic()
            }
            else -> Timber.e("intent?.action : ${intent?.action}")
        }
        return START_NOT_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun startForegroundService(flag: Int) {
        val notification = MusicNotification.createNotification(this, currentMusicTitle, flag)
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun stopForegroundService() {
        stopForeground(true)
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun playCurrentMusic() {
        Timber.d("currentMusicNumber: $currentMusicIndex ")
        currentMusic = musicPlayList[currentMusicIndex]
        currentMusicTitle = musicTitleList[currentMusicIndex]
        startForegroundService(NotificationManager.IMPORTANCE_LOW)
        mediaPlayer = MediaPlayer.create(this, currentMusic).apply {
            isLooping = false
            start()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
