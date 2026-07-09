package com.example.worldcupexplorer.presentation.notifications

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.worldcupexplorer.data.repository.PushNotificationRepository
import com.example.worldcupexplorer.domain.model.PushMessage
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val pushNotificationRepository: PushNotificationRepository
) : ViewModel() {

    val lastMessage: StateFlow<PushMessage?> = pushNotificationRepository.lastMessage

    init {
        // El token identifica a este dispositivo; se registra para poder
        // enviar mensajes de prueba desde Firebase Console.
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                pushNotificationRepository.onNewToken(token)
            }
            .addOnFailureListener { error ->
                Log.e("FCM", "No se pudo obtener el token", error)
            }
    }

    fun dismissMessage() {
        pushNotificationRepository.clearLastMessage()
    }
}
