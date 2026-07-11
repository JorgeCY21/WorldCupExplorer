package com.example.worldcupexplorer.presentation.teams

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.worldcupexplorer.domain.model.Team
import com.example.worldcupexplorer.presentation.common.UiState
import com.example.worldcupexplorer.presentation.common.toCountryFlagUrl
import com.example.worldcupexplorer.presentation.components.AppBackground
import com.example.worldcupexplorer.presentation.components.AppImage
import com.example.worldcupexplorer.presentation.components.AppTopBar
import com.example.worldcupexplorer.presentation.components.CardSubtitle
import com.example.worldcupexplorer.presentation.components.EmptyState
import com.example.worldcupexplorer.presentation.components.HeroBanner
import com.example.worldcupexplorer.presentation.components.ScreenStateContent
import com.example.worldcupexplorer.presentation.components.SearchBar

@Composable
fun TeamsScreen(
    uiState: UiState<List<Team>>,
    onRetry: () -> Unit,
    onTeamClick: (Int) -> Unit
) {
    var query by remember { mutableStateOf("") }

    AppBackground {
        Scaffold(
            topBar = { AppTopBar(title = "Teams") },
            containerColor = Color.Transparent
        ) { innerPadding ->
            ScreenStateContent(
                state = uiState,
                onRetry = onRetry,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) { teams ->
                val filteredTeams = teams.filter { team ->
                    val haystack = listOfNotNull(
                        team.name,
                        team.country,
                        team.coachName
                    ).joinToString(" ").lowercase()
                    query.lowercase() in haystack
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        HeroBanner(
                            title = "Tournament teams",
                            subtitle = "Search every squad, coach and country",
                            detail = "${teams.size} teams available in the competition",
                            emblem = "🛡️"
                        )
                    }
                    item {
                        SearchBar(
                            value = query,
                            onValueChange = { query = it },
                            placeholder = "Search by team, coach or country"
                        )
                    }
                    if (filteredTeams.isEmpty()) {
                        item { EmptyState(message = "No teams match your search.") }
                    } else {
                        items(filteredTeams, key = { it.id }) { team ->
                            TeamCard(team = team, onClick = { onTeamClick(team.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TeamCard(
    team: Team,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppImage(
                imageUrl = team.countryCode.toCountryFlagUrl() ?: team.flagUrl ?: team.crestUrl,
                contentDescription = "${team.name} flag",
                modifier = Modifier.width(64.dp).height(44.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(text = team.name, style = MaterialTheme.typography.titleMedium)
                CardSubtitle(text = "🧠 ${team.coachName.orDash()}")
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

private fun String?.orDash(): String = if (isNullOrBlank()) "N/A" else this
