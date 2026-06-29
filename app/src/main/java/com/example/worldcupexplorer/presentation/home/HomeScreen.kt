package com.example.worldcupexplorer.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.worldcupexplorer.domain.model.HomeDashboard
import com.example.worldcupexplorer.presentation.common.UiState
import com.example.worldcupexplorer.presentation.components.AppBackground
import com.example.worldcupexplorer.presentation.components.AppTopBar
import com.example.worldcupexplorer.presentation.components.CardSubtitle
import com.example.worldcupexplorer.presentation.components.HeroBanner
import com.example.worldcupexplorer.presentation.components.InfoCard
import com.example.worldcupexplorer.presentation.components.NavigationCard
import com.example.worldcupexplorer.presentation.components.ScreenStateContent
import com.example.worldcupexplorer.presentation.components.SectionTitle

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
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    item { SectionTitle(title = "Competition") }
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Current season",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                text = dashboard.competition.currentSeason?.let { season ->
                                    "${season.startDate.orDash()} to ${season.endDate.orDash()}"
                                } ?: "N/A",
                                style = MaterialTheme.typography.titleMedium
                            )
                            CardSubtitle(
                                text = "Matchday ${dashboard.competition.currentSeason?.currentMatchday?.toString() ?: "N/A"}"
                            )
                        }
                    }
                    item { SectionTitle(title = "Statistics") }
                    item {
                        androidx.compose.foundation.layout.Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InfoCard(
                                title = "Teams",
                                value = dashboard.teamsCount.toString(),
                                modifier = Modifier.weight(1f)
                            )
                            InfoCard(
                                title = "Matches",
                                value = dashboard.matchesCount.toString(),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    item {
                        androidx.compose.foundation.layout.Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InfoCard(
                                title = "Scorers",
                                value = dashboard.scorersCount.toString(),
                                modifier = Modifier.weight(1f)
                            )
                            InfoCard(
                                title = "Groups",
                                value = dashboard.groupsCount.toString(),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    item { SectionTitle(title = "Explore") }
                    item {
                        NavigationCard(
                            title = "Teams",
                            subtitle = "Browse squads, coaches and crests",
                            onClick = onTeamsClick
                        )
                    }
                    item {
                        NavigationCard(
                            title = "Matches",
                            subtitle = "Follow fixtures, times and scores",
                            onClick = onMatchesClick
                        )
                    }
                    item {
                        NavigationCard(
                            title = "Standings",
                            subtitle = "Check group tables at a glance",
                            onClick = onStandingsClick
                        )
                    }
                    item {
                        NavigationCard(
                            title = "Top Scorers",
                            subtitle = "See the scoring leaders",
                            onClick = onScorersClick
                        )
                    }
                    item {
                        NavigationCard(
                            title = "About",
                            subtitle = "App and data credits",
                            onClick = onAboutClick
                        )
                    }
                }
            }
        }
    }
}

private fun String?.orDash(): String = if (isNullOrBlank()) "N/A" else this
