package com.example.worldcupexplorer.presentation.standings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.worldcupexplorer.domain.model.StandingEntry
import com.example.worldcupexplorer.domain.model.StandingGroup
import com.example.worldcupexplorer.presentation.common.UiState
import com.example.worldcupexplorer.presentation.components.AppBackground
import com.example.worldcupexplorer.presentation.components.AppTopBar
import com.example.worldcupexplorer.presentation.components.HeroBanner
import com.example.worldcupexplorer.presentation.components.ScreenStateContent
import com.example.worldcupexplorer.presentation.components.SectionTitle

@Composable
fun StandingsScreen(
    uiState: UiState<List<StandingGroup>>,
    onRetry: () -> Unit
) {
    AppBackground {
        Scaffold(
            topBar = { AppTopBar(title = "Standings") },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { innerPadding ->
            ScreenStateContent(
                state = uiState,
                onRetry = onRetry,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) { groups ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        HeroBanner(
                            title = "Standings",
                            subtitle = "Groups, points and qualification picture",
                            detail = "${groups.size} tables available"
                        )
                    }
                    items(groups) { group ->
                        StandingGroupCard(group = group)
                    }
                }
            }
        }
    }
}

@Composable
private fun StandingGroupCard(group: StandingGroup) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle(title = group.group?.let { "Group $it" } ?: group.stage.orDash())
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StandingHeader("Pos", Modifier.weight(1f))
                    StandingHeader("Team", Modifier.weight(3f))
                    StandingHeader("P", Modifier.weight(1f))
                    StandingHeader("W", Modifier.weight(1f))
                    StandingHeader("D", Modifier.weight(1f))
                    StandingHeader("L", Modifier.weight(1f))
                    StandingHeader("GD", Modifier.weight(1f))
                    StandingHeader("Pts", Modifier.weight(1f))
                }
                group.table.forEach { entry ->
                    StandingRow(entry = entry)
                }
            }
        }
    }
}

@Composable
private fun StandingHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
    )
}

@Composable
private fun StandingRow(entry: StandingEntry) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        StandingCell(text = entry.position.toString(), modifier = Modifier.weight(1f))
        StandingCell(text = entry.team.name, modifier = Modifier.weight(3f))
        StandingCell(text = entry.playedGames.toString(), modifier = Modifier.weight(1f))
        StandingCell(text = entry.won.toString(), modifier = Modifier.weight(1f))
        StandingCell(text = entry.draw.toString(), modifier = Modifier.weight(1f))
        StandingCell(text = entry.lost.toString(), modifier = Modifier.weight(1f))
        StandingCell(text = entry.goalDifference.toString(), modifier = Modifier.weight(1f))
        StandingCell(text = entry.points.toString(), modifier = Modifier.weight(1f))
    }
}

@Composable
private fun StandingCell(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}

private fun String?.orDash(): String = if (isNullOrBlank()) "N/A" else this
