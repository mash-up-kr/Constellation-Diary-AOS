package com.mashup.telltostar

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mashup.telltostar.util.NotificationUtil
import timber.log.Timber

class StarStarMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.d("onMessageReceived()")

        if (message.data.isNotEmpty()) {
            val title = message.data["title"]
            val body = message.data["body"]
            val type = message.data["type"]

            if (!title.isNullOrEmpty() && !body.isNullOrEmpty()) {

                val notificationType = when (type) {
                    NotificationUtil.NotificationType.QUESTION.name -> {
                        NotificationUtil.NotificationType.QUESTION
                    }
                    NotificationUtil.NotificationType.HOROSCOPE.name -> {
                        NotificationUtil.NotificationType.HOROSCOPE
                    }
                    else -> {
                        NotificationUtil.NotificationType.NONE
                    }
                }

                Timber.d("title : $title , body : $body , notificationType : $notificationType")
                NotificationUtil.generate(applicationContext, title, body, notificationType)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Timber.d("onNewToken()")
        Timber.d("token $token")
    }
}
