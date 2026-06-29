package com.example.worldcupexplorer.presentation.teams

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.worldcupexplorer.domain.model.Team
import com.example.worldcupexplorer.presentation.common.UiState
import com.example.worldcupexplorer.presentation.common.toCountryFlagUrl
import com.example.worldcupexplorer.presentation.components.AppBackground
import com.example.worldcupexplorer.presentation.components.AppImage
import com.example.worldcupexplorer.presentation.components.AppTopBar
import com.example.worldcupexplorer.presentation.components.CardSubtitle
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
            containerColor = androidx.compose.ui.graphics.Color.Transparent
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
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        HeroBanner(
                            title = "Tournament teams",
                            subtitle = "Search every squad, coach and country",
                            detail = "${teams.size} teams available in the competition"
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
                        item {
                            Text(text = "No teams match your search.")
                        }
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
        onClick = onClick
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppImage(
                imageUrl = team.countryCode.toCountryFlagUrl() ?: team.flagUrl ?: team.crestUrl,
                contentDescription = "${team.name} flag",
                modifier = Modifier
                    .size(72.dp)
                    .padding(top = 4.dp)
            )
            Column(
                modifier = Modifier.weight(0.75f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = team.name, style = MaterialTheme.typography.titleMedium)
                CardSubtitle(text = "Coach: ${team.coachName.orDash()}")
                CardSubtitle(text = "Country: ${team.country.orDash()}")
            }
        }
    }
}

private fun String?.orDash(): String = if (isNullOrBlank()) "N/A" else this
