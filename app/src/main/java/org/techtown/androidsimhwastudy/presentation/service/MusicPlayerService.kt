package org.techtown.androidsimhwastudy.presentation.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi

class MusicPlayerService : Service() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Action Received = ${intent?.action}")
        // intent가 시스템에 의해 재생성되었을때 null값이므로 이 부분도 고려하여 처리
        // 상수 담은 Actions에 각 상황에 따라 로그 찍기 및 관련 서비스 호출
        when (intent?.action) {
            Actions.START_FOREGROUND -> {
                Log.d(TAG, "MusicPlayerService - onStartCommand()-Start Foreground} ")
                startForegroundService()
            }
            Actions.STOP_FOREGROUND -> {
                Log.d(TAG, "MusicPlayerService - onStartCommand()-STOP_FOREGROUND}")
                stopForegroundService()
            }
            Actions.PREV -> Log.d(TAG, "MusicPlayerService - onStartCommand()- PREV")
            Actions.PLAY -> Log.d(TAG, "MusicPlayerService - onStartCommand()- PLAY")
            Actions.NEXT -> Log.d(TAG, "MusicPlayerService - onStartCommand()- NEXT")
            else -> Log.e(TAG, "        null오류 null오류 null오류 null오류 ")
        }
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun startForegroundService() {
        val notification = MusicNotification.createNotification(this)
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun stopForegroundService() {
        stopForeground(true)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "MusicPlayerService - onCreate() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MusicPlayerService - onDestroy() called")
    }

    companion object {
        const val TAG = "[MusicPlayerService]"
        const val NOTIFICATION_ID = 20
    }
}
