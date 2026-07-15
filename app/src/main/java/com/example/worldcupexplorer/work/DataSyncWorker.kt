package com.example.worldcupexplorer.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.worldcupexplorer.domain.repository.FootballRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * CoroutineWorker que refresca el cache local (competicion, equipos, partidos,
 * posiciones y goleadores) llamando al mismo pipeline que usa el auto-sync al
 * recuperar conectividad. Pensado para encolarse solo o como primer eslabon de
 * una cadena de trabajo (ver [BackgroundSyncScheduler]).
 */
@HiltWorker
class DataSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: FootballRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return repository.syncNow().fold(
            onSuccess = { Result.success(workDataOf(KEY_SYNCED to true)) },
            onFailure = {
                if (runAttemptCount < MAX_RETRIES) Result.retry() else Result.failure()
            }
        )
    }

    companion object {
        const val KEY_SYNCED = "synced"
        private const val MAX_RETRIES = 3
    }
}
