package com.example.worldcupexplorer.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveMatchScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun start(simulateFirstCheck: Boolean = false) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            Intent(context, LiveMatchCheckReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // setRepeating es inexacto desde API 19: el sistema agrupa alarmas para
        // ahorrar bateria, asi que cada disparo llega ~5 min despues del anterior.
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + INTERVAL_MILLIS,
            INTERVAL_MILLIS,
            pendingIntent
        )
        Log.d("FCM", "Live match checks scheduled every ${INTERVAL_MILLIS / 60_000} min")

        // Chequeo inmediato al abrir la app; los siguientes llegan por la alarma.
        context.sendBroadcast(
            Intent(context, LiveMatchCheckReceiver::class.java).apply {
                putExtra(LiveMatchCheckReceiver.EXTRA_SIMULATE, simulateFirstCheck)
            }
        )
    }

    private companion object {
        const val REQUEST_CODE = 100
        const val INTERVAL_MILLIS = 5 * 60 * 1000L
    }
}
