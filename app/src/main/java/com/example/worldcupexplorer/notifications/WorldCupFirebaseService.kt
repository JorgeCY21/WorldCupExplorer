package com.example.worldcupexplorer.notifications

import android.util.Log
import com.example.worldcupexplorer.data.repository.PushNotificationRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WorldCupFirebaseService : FirebaseMessagingService() {

    @Inject
    lateinit var pushNotificationRepository: PushNotificationRepository

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Aqui normalmente se envia el token al backend; el lab solo requiere registrarlo.
        pushNotificationRepository.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FCM", "Data: ${message.data}, Notification: ${message.notification?.title}")

        val push = pushNotificationRepository.onMessageReceived(
            data = message.data,
            fallbackTitle = message.notification?.title,
            fallbackBody = message.notification?.body
        )

        // Con la app en primer plano los Notification Messages no se muestran
        // automaticamente, asi que la notificacion local cubre ambos casos.
        notificationHelper.showNotification(push)
    }
}
