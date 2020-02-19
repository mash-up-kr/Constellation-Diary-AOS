package com.mashup.telltostar

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mashup.telltostar.util.NotificationUtil

class StarStarMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        timber.log.Timber.d("onMessageReceived()")

        if (message.data.isNotEmpty()) {
            /*val iterator = message.data.iterator()
            while (iterator.hasNext()) {
                with(iterator.next()) {
                    timber.log.Timber.d("key: $key, value: $value")

                     }
            }*/
            val title = message.data["title"]
            val message = message.data["message"]

            if (!title.isNullOrEmpty() && !message.isNullOrEmpty()) {
                NotificationUtil.generate(applicationContext, title, message)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        timber.log.Timber.d("onNewToken()")
        timber.log.Timber.d("token $token")
    }
}
