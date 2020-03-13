package com.mashup.telltostar.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mashup.telltostar.R
import com.mashup.telltostar.ui.main.MainActivity


object NotificationUtil {

    fun generate(context: Context, title: String, message: String) {
        val channelId = "star"
        val channelName = "Start Notification"

        val notifyManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(
                channelId, channelName, importance
            )
            notifyManager.createNotificationChannel(mChannel)
        }

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context.applicationContext, channelId)

        val notificationIntent =
            Intent(context.applicationContext, MainActivity::class.java).apply {
                putExtra(MainActivity.TYPE, MainActivity.TYPE_RESTART)
            }
        val requestID = System.currentTimeMillis().toInt()

        val pendingIntent = PendingIntent.getActivity(
            context.applicationContext
            , requestID
            , notificationIntent
            , PendingIntent.FLAG_UPDATE_CURRENT
        )

        builder.setContentTitle(title)
            .setContentText(message)
            .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
            .setAutoCancel(true) // 알림 터치시 반응 후 삭제
            .setSound(
                RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)

        notifyManager.notify(0, builder.build())
    }
}