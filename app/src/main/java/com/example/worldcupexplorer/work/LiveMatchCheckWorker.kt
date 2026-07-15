package com.example.worldcupexplorer.work

import android.util.Log
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.worldcupexplorer.data.remote.mapper.toDomain
import com.example.worldcupexplorer.data.remote.source.FootballRemoteDataSource
import com.example.worldcupexplorer.notifications.LiveMatchNotifier
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * CoroutineWorker que revisa si hay partidos en vivo y dispara la notificacion
 * correspondiente. Reemplaza al antiguo LiveMatchCheckReceiver basado en
 * AlarmManager: WorkManager sobrevive al reinicio del dispositivo y respeta
 * las restricciones de red definidas en [BackgroundSyncScheduler].
 */
@HiltWorker
class LiveMatchCheckWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val remoteDataSource: FootballRemoteDataSource,
    private val liveMatchNotifier: LiveMatchNotifier
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val simulate = inputData.getBoolean(KEY_SIMULATE, false)

        return try {
            val matches = remoteDataSource.getWorldCupMatches()
                .matches
                .map { it.toDomain() }

            val liveMatches = matches.filter { match ->
                match.status?.uppercase() in LIVE_STATUSES
            }

            // Modo demo: si no hay partido en vivo, usa el primero con marcador falso.
            val toNotify = when {
                liveMatches.isNotEmpty() -> liveMatches
                simulate -> matches.take(1).map {
                    it.copy(status = "IN_PLAY", score = "2 - 1", stage = it.stage)
                }
                else -> emptyList()
            }

            Log.d(TAG, "Live check: ${liveMatches.size} live of ${matches.size} matches (simulate=$simulate)")
            toNotify.forEach { liveMatchNotifier.notifyLiveMatch(it) }

            Result.success(workDataOf(KEY_LIVE_COUNT to toNotify.size))
        } catch (error: Exception) {
            Log.e(TAG, "Live match check failed", error)
            if (runAttemptCount < MAX_RETRIES) Result.retry() else Result.failure()
        }
    }

    companion object {
        const val KEY_SIMULATE = "simulate"
        const val KEY_LIVE_COUNT = "liveCount"
        private const val TAG = "FCM"
        private const val MAX_RETRIES = 2
        private val LIVE_STATUSES = setOf("LIVE", "IN_PLAY", "PAUSED")
    }
}
