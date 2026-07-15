package com.example.worldcupexplorer.presentation.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.worldcupexplorer.presentation.common.UiState
import com.example.worldcupexplorer.presentation.components.AppBackground
import com.example.worldcupexplorer.presentation.components.AppTopBar
import com.example.worldcupexplorer.presentation.components.HeroBanner
import com.example.worldcupexplorer.presentation.components.ScreenStateContent
import com.example.worldcupexplorer.ui.theme.WorldCupBlue
import com.example.worldcupexplorer.ui.theme.WorldCupGreen

@Composable
fun AboutScreen(
    uiState: UiState<AboutUiModel>,
    syncStatus: SyncStatus,
    onRetry: () -> Unit,
    onSyncNow: () -> Unit
) {
    AppBackground {
        Scaffold(
            topBar = { AppTopBar(title = "About") },
            containerColor = Color.Transparent
        ) { innerPadding ->
            ScreenStateContent(
                state = uiState,
                onRetry = onRetry,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) { about ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        HeroBanner(
                            title = about.appName,
                            subtitle = "A polished World Cup explorer built for the tournament",
                            detail = about.note,
                            emblem = "🌍"
                        )
                    }
                    item {
                        AboutCard(
                            emblem = "🔌",
                            accent = WorldCupBlue,
                            title = "Data source",
                            lines = listOf(about.apiName, about.apiCredit)
                        )
                    }
                    item {
                        AboutCard(
                            emblem = "🛠️",
                            accent = WorldCupGreen,
                            title = "Built with",
                            lines = listOf(
                                "Jetpack Compose · Material 3",
                                "MVVM · Hilt · Room · Retrofit",
                                "Firebase Cloud Messaging · WorkManager"
                            )
                        )
                    }
                    item {
                        SyncNowCard(syncStatus = syncStatus, onSyncNow = onSyncNow)
                    }
                }
            }
        }
    }
}

@Composable
private fun AboutCard(
    emblem: String,
    accent: Color,
    title: String,
    lines: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(accent.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emblem, style = MaterialTheme.typography.titleLarge)
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                lines.forEach { line ->
                    Text(
                        text = line,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SyncNowCard(
    syncStatus: SyncStatus,
    onSyncNow: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(WorldCupBlue.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🔄", style = MaterialTheme.typography.titleLarge)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Background sync", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "WorkManager refreshes data and checks live matches",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                text = syncStatus.toStatusLabel(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(
                onClick = onSyncNow,
                enabled = syncStatus != SyncStatus.Running,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = WorldCupBlue)
            ) {
                if (syncStatus == SyncStatus.Running) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Sync now")
                }
            }
        }
    }
}

private fun SyncStatus.toStatusLabel(): String = when (this) {
    SyncStatus.Idle -> "No sync running yet."
    SyncStatus.Running -> "Syncing data and checking live matches…"
    is SyncStatus.Success -> if (liveMatches > 0) {
        "Synced · $liveMatches live match notification(s) sent."
    } else {
        "Synced · no live matches right now."
    }
    SyncStatus.Failed -> "Sync failed, will retry automatically."
}
