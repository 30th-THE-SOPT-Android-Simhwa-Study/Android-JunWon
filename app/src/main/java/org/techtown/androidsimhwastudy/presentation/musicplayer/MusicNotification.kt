package org.techtown.androidsimhwastudy.presentation.musicplayer

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import org.techtown.androidsimhwastudy.R

object MusicNotification {
    const val CHANNEL_ID = "MusicChannel"
    const val CHANNEL_NAME = "musicChannel"
    const val NOTIFICATION_ID = 1123213
    private var notificationManager: NotificationManager? = null
    private var notification: Notification? = null

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("UnspecifiedImmutableFlag")
    fun createNotification(
        context: Context,
        notificationContentTitle: String,
        flag: Int
    ): Notification? {
        // 알림 클릭시 MusicActivity로 이동됨
        val notificationIntent = Intent(context, MusicActivity::class.java)
        notificationIntent.action = Actions.MAIN
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
            Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getActivity(
                context,
                0,
                notificationIntent,
                FLAG_UPDATE_CURRENT or FLAG_MUTABLE
            )
        } else {
            getActivity(
                context,
                0,
                notificationIntent,
                FLAG_UPDATE_CURRENT
            )
        }

        // 각 버튼들에 관한 Intent
        // prev 버튼
        val prevIntent = Intent(context, MusicPlayerService::class.java).apply {
            action = Actions.PREV
        }
        val prevPendingIntent = PendingIntent
            .getService(context, 0, prevIntent, FLAG_MUTABLE)
        // play 버튼
        val playIntent = Intent(context, MusicPlayerService::class.java).apply {
            action = Actions.PLAY
        }
        val playPendingIntent = PendingIntent
            .getService(context, 0, playIntent, FLAG_MUTABLE)
        // next 버튼
        val nextIntent = Intent(context, MusicPlayerService::class.java).apply {
            action = Actions.NEXT
        }
        val nextPendingIntent = PendingIntent
            .getService(context, 0, nextIntent, FLAG_MUTABLE)
        // create channel
        createNotificationChannel(context, flag)
        // 알림
        notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Music Player")
            .setContentText(notificationContentTitle)
            .setSmallIcon(R.drawable.ic_music_24)
            .setOngoing(true) // true 일경우 알림 리스트에서 클릭하거나 좌우로 드래그해도 사라지지 않음
            .addAction(
                NotificationCompat.Action(
                    android.R.drawable.ic_media_previous,
                    "Prev",
                    prevPendingIntent
                )
            )
            .addAction(
                NotificationCompat.Action(
                    android.R.drawable.ic_media_play,
                    "Play",
                    playPendingIntent
                )
            )
            .addAction(
                NotificationCompat.Action(
                    android.R.drawable.ic_media_next,
                    "Next",
                    nextPendingIntent
                )
            )
            .setContentIntent(pendingIntent)
            .build()
        // 나중에 알림 업데이트 및 제거할 수 있음
        notificationManager?.notify(NOTIFICATION_ID, notification)

        return notification
    }

    private fun createNotificationChannel(context: Context, flag: Int) {
        // Oreo 부터는 Notification Channel을 만들어야 함
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            flag
        )
        notificationManager =
            context.getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(serviceChannel)
    }
}
