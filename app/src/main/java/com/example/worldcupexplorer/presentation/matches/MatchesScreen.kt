package com.example.worldcupexplorer.presentation.matches

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.worldcupexplorer.domain.model.Match
import com.example.worldcupexplorer.presentation.common.UiState
import com.example.worldcupexplorer.presentation.common.toCountryFlagUrl
import com.example.worldcupexplorer.presentation.components.AppBackground
import com.example.worldcupexplorer.presentation.components.AppImage
import com.example.worldcupexplorer.presentation.components.AppTopBar
import com.example.worldcupexplorer.presentation.components.HeroBanner
import com.example.worldcupexplorer.presentation.components.ScreenStateContent
import com.example.worldcupexplorer.presentation.components.StatusChip
import com.example.worldcupexplorer.presentation.components.toDisplayDate
import com.example.worldcupexplorer.presentation.components.toDisplayTime
import com.example.worldcupexplorer.presentation.components.toFriendlyLabel

@Composable
fun MatchesScreen(
    uiState: UiState<List<Match>>,
    onRetry: () -> Unit
) {
    var filter by remember { mutableStateOf("All") }

    AppBackground {
        Scaffold(
            topBar = { AppTopBar(title = "Matches") },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { innerPadding ->
            ScreenStateContent(
                state = uiState,
                onRetry = onRetry,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) { matches ->
                val filtered = when (filter) {
                    "Scheduled" -> matches.filter { it.status.equals("SCHEDULED", ignoreCase = true) }
                    "Finished" -> matches.filter { it.status.equals("FINISHED", ignoreCase = true) }
                    "Live" -> matches.filter { it.status.equals("LIVE", ignoreCase = true) }
                    else -> matches
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        HeroBanner(
                            title = "Fixtures & Results",
                            subtitle = "Follow every match from kickoff to final whistle",
                            detail = "${matches.size} matches loaded"
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
                    if (filtered.isEmpty()) {
                        item { Text(text = "No matches available for this filter.") }
                    } else {
                        items(filtered, key = { it.id }) { match ->
                            MatchCard(match = match)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MatchCard(match: Match) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppImage(
                    imageUrl = match.homeTeam.name.toCountryFlagUrl() ?: match.homeTeam.crestUrl,
                    contentDescription = match.homeTeam.name,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = match.status.toFriendlyLabel(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                AppImage(
                    imageUrl = match.awayTeam.name.toCountryFlagUrl() ?: match.awayTeam.crestUrl,
                    contentDescription = match.awayTeam.name,
                    modifier = Modifier.size(48.dp)
                )
            }
            Text(
                text = "${match.homeTeam.name} vs ${match.awayTeam.name}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Date: ${match.utcDate.toDisplayDate()}  Time: ${match.utcDate.toDisplayTime()}")
            Text(text = "Score: ${match.score.orDash()}", style = MaterialTheme.typography.headlineSmall)
        }
    }
}

private fun String?.orDash(): String = if (isNullOrBlank()) "N/A" else this
