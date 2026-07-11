package com.example.worldcupexplorer.presentation.teamdetails

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.worldcupexplorer.domain.model.Team
import com.example.worldcupexplorer.presentation.common.UiState
import com.example.worldcupexplorer.presentation.common.toCountryFlagUrl
import com.example.worldcupexplorer.presentation.components.AppBackground
import com.example.worldcupexplorer.presentation.components.AppImage
import com.example.worldcupexplorer.presentation.components.AppTopBar
import com.example.worldcupexplorer.presentation.components.EmptyState
import com.example.worldcupexplorer.presentation.components.HeroBanner
import com.example.worldcupexplorer.presentation.components.InfoRow
import com.example.worldcupexplorer.presentation.components.ScreenStateContent
import com.example.worldcupexplorer.presentation.components.SectionTitle
import com.example.worldcupexplorer.ui.theme.WorldCupBlue

@Composable
fun TeamDetailsScreen(
    uiState: UiState<Team>,
    onRetry: () -> Unit,
    onBackClick: () -> Unit
) {
    AppBackground {
        Scaffold(
            topBar = { AppTopBar(title = "Team Details", onBackClick = onBackClick) },
            containerColor = Color.Transparent
        ) { innerPadding ->
            ScreenStateContent(
                state = uiState,
                onRetry = onRetry,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) { team ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        HeroBanner(
                            title = team.name,
                            subtitle = team.country.orDash(),
                            detail = "Coach ${team.coachName.orDash()} • ${team.tla.orDash()}",
                            emblem = "🛡️"
                        )
                    }
                    item { FlagCard(team = team) }
                    item { SectionTitle(title = "Team info") }
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                InfoRow(label = "Coach", value = team.coachName.orDash(), icon = Icons.Default.Person)
                                InfoRow(label = "Venue", value = team.venue.orDash(), icon = Icons.Default.Place)
                                InfoRow(label = "Website", value = team.website.orDash(), icon = Icons.Default.Language)
                                InfoRow(label = "Address", value = team.address.orDash(), icon = Icons.Default.Home)
                                InfoRow(label = "Club Colors", value = team.clubColors.orDash(), icon = Icons.Default.Palette)
                            }
                        }
                    }
                    item { SectionTitle(title = "Squad · ${team.squad.size} players") }
                    if (team.squad.isEmpty()) {
                        item { EmptyState(message = "No squad data available.", emblem = "🧑‍🤝‍🧑") }
                    } else {
                        items(team.squad, key = { it.id ?: it.name }) { player ->
                            PlayerCard(
                                playerName = player.name,
                                position = player.position,
                                nationality = player.nationality,
                                dateOfBirth = player.dateOfBirth
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FlagCard(team: Team) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppImage(
                imageUrl = team.countryCode.toCountryFlagUrl() ?: team.flagUrl ?: team.crestUrl,
                contentDescription = "${team.name} flag",
                modifier = Modifier
                    .width(132.dp)
                    .height(86.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Text(text = team.country.orDash(), style = MaterialTheme.typography.titleMedium)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = team.tla.orDash(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun PlayerCard(
    playerName: String,
    position: String?,
    nationality: String?,
    dateOfBirth: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(WorldCupBlue.copy(alpha = 0.10f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = playerName.initials(),
                    style = MaterialTheme.typography.titleSmall,
                    color = WorldCupBlue
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(text = playerName, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = listOfNotNull(
                        nationality,
                        dateOfBirth?.let { "🎂 $it" }
                    ).joinToString(" · ").ifBlank { "N/A" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!position.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = position,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

private fun String.initials(): String =
    split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

private fun String?.orDash(): String = if (isNullOrBlank()) "N/A" else this
