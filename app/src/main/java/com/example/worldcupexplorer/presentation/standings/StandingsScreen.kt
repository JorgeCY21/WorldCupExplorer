package com.example.worldcupexplorer.presentation.standings

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.worldcupexplorer.domain.model.StandingEntry
import com.example.worldcupexplorer.domain.model.StandingGroup
import com.example.worldcupexplorer.presentation.common.UiState
import com.example.worldcupexplorer.presentation.components.AppBackground
import com.example.worldcupexplorer.presentation.components.AppTopBar
import com.example.worldcupexplorer.presentation.components.HeroBanner
import com.example.worldcupexplorer.presentation.components.ScreenStateContent
import com.example.worldcupexplorer.presentation.components.SectionTitle
import com.example.worldcupexplorer.ui.theme.WorldCupGreen

@Composable
fun StandingsScreen(
    uiState: UiState<List<StandingGroup>>,
    onRetry: () -> Unit
) {
    AppBackground {
        Scaffold(
            topBar = { AppTopBar(title = "Standings") },
            containerColor = Color.Transparent
        ) { innerPadding ->
            ScreenStateContent(
                state = uiState,
                onRetry = onRetry,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) { groups ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        HeroBanner(
                            title = "Standings",
                            subtitle = "Groups, points and qualification picture",
                            detail = "${groups.size} tables available",
                            emblem = "📊"
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
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionTitle(
            title = group.group
                ?.replace('_', ' ')
                ?.lowercase()
                ?.replaceFirstChar { it.uppercase() }
                ?.let { if (it.startsWith("Group", ignoreCase = true)) it else "Group $it" }
                ?: group.stage.orDash()
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HeaderCell("#", Modifier.size(26.dp))
                    HeaderCell("Team", Modifier.weight(2.4f).padding(start = 10.dp), TextAlign.Start)
                    HeaderCell("PJ", Modifier.weight(0.7f))
                    HeaderCell("G", Modifier.weight(0.7f))
                    HeaderCell("E", Modifier.weight(0.7f))
                    HeaderCell("P", Modifier.weight(0.7f))
                    HeaderCell("DG", Modifier.weight(0.8f))
                    HeaderCell("Pts", Modifier.weight(0.9f))
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                group.table.forEachIndexed { index, entry ->
                    StandingRow(entry = entry, isQualifying = entry.position <= 2)
                    if (index != group.table.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderCell(
    title: String,
    modifier: Modifier = Modifier,
    align: TextAlign = TextAlign.Center
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = align,
        modifier = modifier
    )
}

@Composable
private fun StandingRow(entry: StandingEntry, isQualifying: Boolean) {
    val accent = if (isQualifying) WorldCupGreen else MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isQualifying) WorldCupGreen.copy(alpha = 0.06f) else Color.Transparent
            )
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .background(accent.copy(alpha = if (isQualifying) 0.9f else 0.14f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = entry.position.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = if (isQualifying) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = entry.team.name,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(2.4f).padding(start = 10.dp)
        )
        BodyCell(entry.playedGames.toString(), Modifier.weight(0.7f))
        BodyCell(entry.won.toString(), Modifier.weight(0.7f))
        BodyCell(entry.draw.toString(), Modifier.weight(0.7f))
        BodyCell(entry.lost.toString(), Modifier.weight(0.7f))
        BodyCell(
            text = if (entry.goalDifference > 0) "+${entry.goalDifference}" else entry.goalDifference.toString(),
            modifier = Modifier.weight(0.8f)
        )
        Text(
            text = entry.points.toString(),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.9f)
        )
    }
}

@Composable
private fun BodyCell(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

private fun String?.orDash(): String = if (isNullOrBlank()) "N/A" else this
