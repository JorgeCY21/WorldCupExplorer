package com.example.worldcupexplorer.work

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

/**
 * Punto unico para encolar trabajo en segundo plano con WorkManager.
 *
 * - [schedulePeriodicLiveMatchCheck]: PeriodicWorkRequest con restriccion de red,
 *   reemplaza el AlarmManager.setRepeating anterior (15 min es el minimo que
 *   permite Android para trabajo periodico).
 * - [runSyncNowChain]: encadena DataSyncWorker -> LiveMatchCheckWorker
 *   (Work Chaining) para refrescar el cache y avisar de partidos en vivo con
 *   una sola llamada, tanto al abrir la app como desde un boton manual.
 */
@Singleton
class BackgroundSyncScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val workManager: WorkManager
        get() = WorkManager.getInstance(context)

    private val networkConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    fun schedulePeriodicLiveMatchCheck() {
        val request = PeriodicWorkRequestBuilder<LiveMatchCheckWorker>(
            PERIODIC_INTERVAL_MINUTES, TimeUnit.MINUTES
        )
            .setConstraints(networkConstraints)
            .setBackoffCriteria(BackoffPolicy.LINEAR, BACKOFF_DELAY_MINUTES, TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniquePeriodicWork(
            LIVE_MATCH_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    fun runSyncNowChain(simulateLiveMatch: Boolean = false) {
        val syncRequest = OneTimeWorkRequestBuilder<DataSyncWorker>()
            .setConstraints(networkConstraints)
            .build()

        val liveCheckRequest = OneTimeWorkRequestBuilder<LiveMatchCheckWorker>()
            .setConstraints(networkConstraints)
            .setInputData(workDataOf(LiveMatchCheckWorker.KEY_SIMULATE to simulateLiveMatch))
            .build()

        workManager
            .beginUniqueWork(SYNC_CHAIN_WORK_NAME, ExistingWorkPolicy.REPLACE, syncRequest)
            .then(liveCheckRequest)
            .enqueue()
    }

    fun observeSyncChainStatus(): Flow<List<WorkInfo>> =
        workManager.getWorkInfosForUniqueWorkFlow(SYNC_CHAIN_WORK_NAME)

    private companion object {
        const val LIVE_MATCH_WORK_NAME = "live_match_periodic_check"
        const val SYNC_CHAIN_WORK_NAME = "data_sync_chain"
        const val PERIODIC_INTERVAL_MINUTES = 15L
        const val BACKOFF_DELAY_MINUTES = 5L
    }
}
