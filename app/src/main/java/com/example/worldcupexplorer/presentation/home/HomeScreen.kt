package com.example.worldcupexplorer.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.worldcupexplorer.domain.model.HomeDashboard
import com.example.worldcupexplorer.presentation.common.UiState
import com.example.worldcupexplorer.presentation.components.AppBackground
import com.example.worldcupexplorer.presentation.components.AppTopBar
import com.example.worldcupexplorer.presentation.components.HeroBanner
import com.example.worldcupexplorer.presentation.components.InfoCard
import com.example.worldcupexplorer.presentation.components.NavigationCard
import com.example.worldcupexplorer.presentation.components.ScreenStateContent
import com.example.worldcupexplorer.presentation.components.SectionTitle
import com.example.worldcupexplorer.ui.theme.WorldCupBlue
import com.example.worldcupexplorer.ui.theme.WorldCupCrimson
import com.example.worldcupexplorer.ui.theme.WorldCupGold
import com.example.worldcupexplorer.ui.theme.WorldCupGreen
import com.example.worldcupexplorer.ui.theme.WorldCupRed

@Composable
fun HomeScreen(
    uiState: UiState<HomeDashboard>,
    onRetry: () -> Unit,
    onTeamsClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onStandingsClick: () -> Unit,
    onScorersClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    AppBackground {
        Scaffold(
            topBar = { AppTopBar(title = "World Cup Explorer") },
            containerColor = Color.Transparent
        ) { innerPadding ->
            ScreenStateContent(
                state = uiState,
                onRetry = onRetry,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) { dashboard ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        HeroBanner(
                            title = dashboard.competition.name,
                            subtitle = "World Cup dashboard and live tournament overview",
                            detail = buildString {
                                append("Code ")
                                append(dashboard.competition.code.ifBlank { "WC" })
                                dashboard.competition.areaName?.let {
                                    append(" • ")
                                    append(it)
                                }
                            }
                        )
                    }
                    item { SeasonCard(dashboard) }
                    item { SectionTitle(title = "Tournament stats") }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InfoCard(
                                title = "Teams",
                                value = dashboard.teamsCount.toString(),
                                emblem = "🛡️",
                                accent = WorldCupBlue,
                                modifier = Modifier.weight(1f)
                            )
                            InfoCard(
                                title = "Matches",
                                value = dashboard.matchesCount.toString(),
                                emblem = "⚽",
                                accent = WorldCupGreen,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InfoCard(
                                title = "Scorers",
                                value = dashboard.scorersCount.toString(),
                                emblem = "👟",
                                accent = WorldCupCrimson,
                                modifier = Modifier.weight(1f)
                            )
                            InfoCard(
                                title = "Groups",
                                value = dashboard.groupsCount.toString(),
                                emblem = "🏟️",
                                accent = WorldCupGold,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    item { SectionTitle(title = "Explore") }
                    item {
                        NavigationCard(
                            title = "Teams",
                            subtitle = "Browse squads, coaches and crests",
                            emblem = "🛡️",
                            accent = WorldCupBlue,
                            onClick = onTeamsClick
                        )
                    }
                    item {
                        NavigationCard(
                            title = "Matches",
                            subtitle = "Follow fixtures, times and scores",
                            emblem = "⚽",
                            accent = WorldCupGreen,
                            onClick = onMatchesClick
                        )
                    }
                    item {
                        NavigationCard(
                            title = "Standings",
                            subtitle = "Check group tables at a glance",
                            emblem = "📊",
                            accent = WorldCupGold,
                            onClick = onStandingsClick
                        )
                    }
                    item {
                        NavigationCard(
                            title = "Top Scorers",
                            subtitle = "See the scoring leaders",
                            emblem = "🥇",
                            accent = WorldCupCrimson,
                            onClick = onScorersClick
                        )
                    }
                    item {
                        NavigationCard(
                            title = "About",
                            subtitle = "App and data credits",
                            emblem = "ℹ️",
                            accent = WorldCupRed,
                            onClick = onAboutClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SeasonCard(dashboard: HomeDashboard) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "CURRENT SEASON",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = dashboard.competition.currentSeason?.let { season ->
                        "${season.startDate.orDash()} → ${season.endDate.orDash()}"
                    } ?: "N/A",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.End
            ) {
                Text(
                    text = "MATCHDAY",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = dashboard.competition.currentSeason?.currentMatchday?.toString() ?: "—",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

private fun String?.orDash(): String = if (isNullOrBlank()) "N/A" else this
