package com.example.worldcupexplorer.presentation.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.worldcupexplorer.domain.model.Match
import com.example.worldcupexplorer.domain.model.MatchTeam
import com.example.worldcupexplorer.presentation.common.UiState
import com.example.worldcupexplorer.presentation.common.toCountryFlagUrl
import com.example.worldcupexplorer.presentation.components.AppBackground
import com.example.worldcupexplorer.presentation.components.AppImage
import com.example.worldcupexplorer.presentation.components.AppTopBar
import com.example.worldcupexplorer.presentation.components.EmptyState
import com.example.worldcupexplorer.presentation.components.HeroBanner
import com.example.worldcupexplorer.presentation.components.ScreenStateContent
import com.example.worldcupexplorer.presentation.components.SectionTitle
import com.example.worldcupexplorer.presentation.components.StatusChip
import com.example.worldcupexplorer.presentation.components.toDisplayDate
import com.example.worldcupexplorer.presentation.components.toDisplayTime
import com.example.worldcupexplorer.presentation.components.toFriendlyLabel
import com.example.worldcupexplorer.ui.theme.WorldCupCrimson
import com.example.worldcupexplorer.ui.theme.WorldCupGreen

private val LIVE_STATUSES = setOf("LIVE", "IN_PLAY", "PAUSED")

private val STAGE_ORDER = listOf(
    "GROUP_STAGE",
    "LAST_32",
    "LAST_16",
    "ROUND_OF_16",
    "QUARTER_FINALS",
    "SEMI_FINALS",
    "THIRD_PLACE",
    "FINAL"
)

private fun String.toTitleWords(): String =
    replace('_', ' ')
        .lowercase()
        .split(' ')
        .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }

private fun stageRank(stage: String?): Int =
    STAGE_ORDER.indexOf(stage?.uppercase()).let { if (it == -1) STAGE_ORDER.size else it }

private fun stageDisplayName(stage: String?): String = when (stage?.uppercase()) {
    "GROUP_STAGE" -> "Group stage"
    "LAST_32" -> "Round of 32"
    "LAST_16", "ROUND_OF_16" -> "Round of 16"
    "QUARTER_FINALS" -> "Quarter-finals"
    "SEMI_FINALS" -> "Semi-finals"
    "THIRD_PLACE" -> "Third place"
    "FINAL" -> "Final"
    null -> "Other"
    else -> stage.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }
}

private val STAGE_FILTERS = listOf(
    "All stages" to null,
    "Groups" to setOf("GROUP_STAGE"),
    "Round of 32" to setOf("LAST_32"),
    "Round of 16" to setOf("LAST_16", "ROUND_OF_16"),
    "Quarters" to setOf("QUARTER_FINALS"),
    "Semis" to setOf("SEMI_FINALS"),
    "Final" to setOf("THIRD_PLACE", "FINAL")
)

@Composable
fun MatchesScreen(
    uiState: UiState<List<Match>>,
    onRetry: () -> Unit
) {
    var filter by remember { mutableStateOf("All") }
    var stageFilter by remember { mutableStateOf("All stages") }

    AppBackground {
        Scaffold(
            topBar = { AppTopBar(title = "Matches") },
            containerColor = Color.Transparent
        ) { innerPadding ->
            ScreenStateContent(
                state = uiState,
                onRetry = onRetry,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) { matches ->
                val byStatus = when (filter) {
                    "Scheduled" -> matches.filter { it.status.equals("SCHEDULED", ignoreCase = true) || it.status.equals("TIMED", ignoreCase = true) }
                    "Finished" -> matches.filter { it.status.equals("FINISHED", ignoreCase = true) }
                    "Live" -> matches.filter { it.status?.uppercase() in LIVE_STATUSES }
                    else -> matches
                }
                val selectedStages = STAGE_FILTERS.firstOrNull { it.first == stageFilter }?.second
                val filtered = if (selectedStages == null) {
                    byStatus
                } else {
                    byStatus.filter { it.stage?.uppercase() in selectedStages }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        HeroBanner(
                            title = "Fixtures & Results",
                            subtitle = "Follow every match from kickoff to final whistle",
                            detail = "${matches.size} matches loaded",
                            emblem = "⚽"
                        )
                    }
                    item {
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("All", "Scheduled", "Finished", "Live").forEach { option ->
                                StatusChip(
                                    label = option,
                                    selected = filter == option,
                                    onClick = { filter = option }
                                )
                            }
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            STAGE_FILTERS.forEach { (label, _) ->
                                StatusChip(
                                    label = label,
                                    selected = stageFilter == label,
                                    onClick = { stageFilter = label }
                                )
                            }
                        }
                    }
                    if (filtered.isEmpty()) {
                        item { EmptyState(message = "No matches available for this filter.", emblem = "📅") }
                    } else {
                        // Primero lo que falta jugar (y en vivo); lo ya jugado al final.
                        val (pending, played) = filtered.partition {
                            !it.status.equals("FINISHED", ignoreCase = true)
                        }

                        if (pending.isNotEmpty()) {
                            item { SectionTitle(title = "⏳ Upcoming") }
                            stageSections(pending)
                        }
                        if (played.isNotEmpty()) {
                            item { SectionTitle(title = "✅ Played") }
                            stageSections(played)
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.stageSections(matches: List<Match>) {
    matches
        .groupBy { it.stage?.uppercase() }
        .toList()
        .sortedBy { (stage, _) -> stageRank(stage) }
        .forEach { (stage, stageMatches) ->
            item { StageHeader(title = stageDisplayName(stage), count = stageMatches.size) }
            items(stageMatches.sortedBy { it.utcDate.orEmpty() }, key = { it.id }) { match ->
                MatchCard(match = match)
            }
        }
}

@Composable
private fun StageHeader(title: String, count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun MatchCard(match: Match) {
    val isLive = match.status?.uppercase() in LIVE_STATUSES

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = match.group?.toTitleWords() ?: stageDisplayName(match.stage),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                StatusBadge(status = match.status, isLive = isLive)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TeamColumn(team = match.homeTeam, modifier = Modifier.weight(1f))
                Text(
                    text = match.score ?: "vs",
                    style = MaterialTheme.typography.headlineMedium,
                    color = if (isLive) WorldCupCrimson else MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1.1f)
                )
                TeamColumn(team = match.awayTeam, modifier = Modifier.weight(1f))
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📅 ${match.utcDate.toDisplayDate()}   🕑 ${match.utcDate.toDisplayTime()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TeamColumn(team: MatchTeam, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        AppImage(
            imageUrl = team.name.toCountryFlagUrl() ?: team.crestUrl,
            contentDescription = team.name,
            modifier = Modifier.width(56.dp).height(38.dp)
        )
        Text(
            text = team.name,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
private fun StatusBadge(status: String?, isLive: Boolean) {
    val (accent, label) = when {
        isLive -> WorldCupCrimson to "LIVE"
        status.equals("FINISHED", ignoreCase = true) -> WorldCupGreen to "Finished"
        status.equals("TIMED", ignoreCase = true) ||
            status.equals("SCHEDULED", ignoreCase = true) ->
            MaterialTheme.colorScheme.onSurfaceVariant to "Scheduled"
        else -> MaterialTheme.colorScheme.onSurfaceVariant to status.toFriendlyLabel()
    }
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(accent.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        if (isLive) {
            Box(modifier = Modifier.size(7.dp).background(accent, CircleShape))
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = accent
        )
    }
}
