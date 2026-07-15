package com.example.worldcupexplorer.presentation.about

import androidx.work.WorkInfo
import com.example.worldcupexplorer.work.LiveMatchCheckWorker

/** Refleja el estado de la cadena DataSyncWorker -> LiveMatchCheckWorker en la UI. */
sealed interface SyncStatus {
    data object Idle : SyncStatus
    data object Running : SyncStatus
    data class Success(val liveMatches: Int) : SyncStatus
    data object Failed : SyncStatus
}

fun List<WorkInfo>.toSyncStatus(): SyncStatus = when {
    isEmpty() -> SyncStatus.Idle
    any { it.state == WorkInfo.State.FAILED } -> SyncStatus.Failed
    all { it.state == WorkInfo.State.SUCCEEDED } -> SyncStatus.Success(
        liveMatches = firstNotNullOfOrNull { info ->
            info.outputData.getInt(LiveMatchCheckWorker.KEY_LIVE_COUNT, -1).takeIf { it >= 0 }
        } ?: 0
    )
    any { it.state == WorkInfo.State.CANCELLED } -> SyncStatus.Idle
    else -> SyncStatus.Running
}
