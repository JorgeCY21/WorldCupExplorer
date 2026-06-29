package com.example.worldcupexplorer.presentation.teamdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.worldcupexplorer.domain.model.Team
import com.example.worldcupexplorer.presentation.common.UiState
import com.example.worldcupexplorer.presentation.common.toCountryFlagUrl
import com.example.worldcupexplorer.presentation.components.AppBackground
import com.example.worldcupexplorer.presentation.components.AppImage
import com.example.worldcupexplorer.presentation.components.AppTopBar
import com.example.worldcupexplorer.presentation.components.HeroBanner
import com.example.worldcupexplorer.presentation.components.InfoRow
import com.example.worldcupexplorer.presentation.components.ScreenStateContent
import com.example.worldcupexplorer.presentation.components.SectionTitle

@Composable
fun TeamDetailsScreen(
    uiState: UiState<Team>,
    onRetry: () -> Unit,
    onBackClick: () -> Unit
) {
    AppBackground {
        Scaffold(
            topBar = { AppTopBar(title = "Team Details", onBackClick = onBackClick) },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { innerPadding ->
            ScreenStateContent(
                state = uiState,
                onRetry = onRetry,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) { team ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        HeroBanner(
                            title = team.name,
                            subtitle = team.country.orDash(),
                            detail = "Coach ${team.coachName.orDash()} • Venue ${team.venue.orDash()}"
                        )
                    }
                    item {
                        AppImage(
                            imageUrl = team.flagUrl ?: team.countryCode.toCountryFlagUrl() ?: team.crestUrl,
                            contentDescription = "${team.name} flag",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        )
                    }
                    item {
                        AppImage(
                            imageUrl = team.crestUrl,
                            contentDescription = team.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )
                    }
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            InfoRow(label = "Coach", value = team.coachName.orDash())
                            InfoRow(label = "Venue", value = team.venue.orDash())
                            InfoRow(label = "Website", value = team.website.orDash())
                            InfoRow(label = "Address", value = team.address.orDash())
                            InfoRow(label = "Club Colors", value = team.clubColors.orDash())
                        }
                    }
                    item { SectionTitle(title = "Squad") }
                    if (team.squad.isEmpty()) {
                        item { Text(text = "No squad data available.") }
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
private fun PlayerCard(
    playerName: String,
    position: String?,
    nationality: String?,
    dateOfBirth: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(22.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = playerName, style = MaterialTheme.typography.titleMedium)
            Text(text = "Position: ${position.orDash()}")
            Text(text = "Nationality: ${nationality.orDash()}")
            Text(text = "Date of birth: ${dateOfBirth.orDash()}")
        }
    }
}

private fun String?.orDash(): String = if (isNullOrBlank()) "N/A" else this
