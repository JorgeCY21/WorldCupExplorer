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
import com.example.worldcupexplorer.navigation.AppNavGraph
import com.example.worldcupexplorer.notifications.NotificationHelper
import com.example.worldcupexplorer.ui.theme.WorldCupExplorerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var pendingTeamId by mutableStateOf<Int?>(null)

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        askNotificationPermission()
        pendingTeamId = intent.teamIdExtra()
        setContent {
            WorldCupExplorerTheme {
                AppNavGraph(
                    pendingTeamId = pendingTeamId,
                    onPendingTeamIdConsumed = { pendingTeamId = null }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        pendingTeamId = intent.teamIdExtra()
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun Intent.teamIdExtra(): Int? =
        if (hasExtra(NotificationHelper.EXTRA_TEAM_ID)) {
            getIntExtra(NotificationHelper.EXTRA_TEAM_ID, -1).takeIf { it >= 0 }
        } else {
            null
        }
}

@Preview(showBackground = true)
@Composable
private fun MainActivityPreview() {
    WorldCupExplorerTheme {
        AppNavGraph()
    }
}
