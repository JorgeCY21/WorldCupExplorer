package com.example.worldcupexplorer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.worldcupexplorer.navigation.AppDestination
import com.example.worldcupexplorer.navigation.AppNavGraph
import com.example.worldcupexplorer.notifications.LiveMatchScheduler
import com.example.worldcupexplorer.notifications.NotificationHelper
import com.example.worldcupexplorer.ui.theme.WorldCupExplorerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var liveMatchScheduler: LiveMatchScheduler

    private var pendingRoute by mutableStateOf<String?>(null)

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            // Recien aqui hay permiso: el chequeo inmediato ya puede notificar.
            if (granted) {
                startLiveMatchChecks()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        when {
            hasNotificationPermission() -> startLiveMatchChecks()
            // Solo se pregunta la primera vez; si el usuario nego, se respeta.
            !notificationPermissionAlreadyRequested() -> {
                markNotificationPermissionRequested()
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        pendingRoute = intent.pendingRouteExtra()
        setContent {
            WorldCupExplorerTheme {
                AppNavGraph(
                    pendingRoute = pendingRoute,
                    onPendingRouteConsumed = { pendingRoute = null }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        pendingRoute = intent.pendingRouteExtra()
    }

    private fun hasNotificationPermission(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED

    private fun startLiveMatchChecks() {
        liveMatchScheduler.start(
            simulateFirstCheck = intent.getBooleanExtra(EXTRA_SIMULATE_LIVE_CHECK, false)
        )
    }

    private fun notificationPermissionAlreadyRequested(): Boolean =
        getPreferences(MODE_PRIVATE).getBoolean(PREF_PERMISSION_REQUESTED, false)

    private fun markNotificationPermissionRequested() {
        getPreferences(MODE_PRIVATE).edit().putBoolean(PREF_PERMISSION_REQUESTED, true).apply()
    }

    private fun Intent.pendingRouteExtra(): String? = when {
        hasExtra(NotificationHelper.EXTRA_TEAM_ID) ->
            getIntExtra(NotificationHelper.EXTRA_TEAM_ID, -1)
                .takeIf { it >= 0 }
                ?.let { AppDestination.teamDetailsRoute(it) }
        getStringExtra(NotificationHelper.EXTRA_DESTINATION) == NotificationHelper.DESTINATION_MATCHES ->
            AppDestination.MatchesRoute
        else -> null
    }

    private companion object {
        const val EXTRA_SIMULATE_LIVE_CHECK = "simulateLiveCheck"
        const val PREF_PERMISSION_REQUESTED = "notification_permission_requested"
    }
}

@Preview(showBackground = true)
@Composable
private fun MainActivityPreview() {
    WorldCupExplorerTheme {
        AppNavGraph()
    }
}
