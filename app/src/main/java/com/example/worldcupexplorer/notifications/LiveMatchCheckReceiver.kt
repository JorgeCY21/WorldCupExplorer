package com.example.worldcupexplorer.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.worldcupexplorer.data.remote.mapper.toDomain
import com.example.worldcupexplorer.data.remote.source.FootballRemoteDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LiveMatchCheckReceiver : BroadcastReceiver() {

    @Inject
    lateinit var remoteDataSource: FootballRemoteDataSource

    @Inject
    lateinit var liveMatchNotifier: LiveMatchNotifier

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        val simulate = intent.getBooleanExtra(EXTRA_SIMULATE, false)

        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
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
            } catch (error: Exception) {
                Log.e(TAG, "Live match check failed", error)
            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        const val EXTRA_SIMULATE = "simulate"
        private const val TAG = "FCM"
        private val LIVE_STATUSES = setOf("LIVE", "IN_PLAY", "PAUSED")
    }
}
