package com.example.worldcupexplorer.data.repository

import android.util.Log
import com.example.worldcupexplorer.domain.model.PushMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PushNotificationRepository @Inject constructor() {

    private val _lastMessage = MutableStateFlow<PushMessage?>(null)
    val lastMessage: StateFlow<PushMessage?> = _lastMessage.asStateFlow()

    private val _registrationToken = MutableStateFlow<String?>(null)
    val registrationToken: StateFlow<String?> = _registrationToken.asStateFlow()

    fun onMessageReceived(
        data: Map<String, String>,
        fallbackTitle: String?,
        fallbackBody: String?
    ): PushMessage {
        val message = PushMessage(
            title = data["title"] ?: fallbackTitle ?: "World Cup Explorer",
            body = data["body"] ?: fallbackBody.orEmpty(),
            teamId = data["teamId"]?.toIntOrNull()
        )
        Log.d(TAG, "Push message received: $message")
        _lastMessage.value = message
        return message
    }

    fun onNewToken(token: String) {
        Log.d(TAG, "New registration token: $token")
        _registrationToken.value = token
    }

    fun clearLastMessage() {
        _lastMessage.value = null
    }

    private companion object {
        const val TAG = "FCM"
    }
}
