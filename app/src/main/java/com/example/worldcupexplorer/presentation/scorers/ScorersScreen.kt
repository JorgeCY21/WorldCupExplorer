package com.example.worldcupexplorer.presentation.scorers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.example.worldcupexplorer.domain.model.Scorer
import com.example.worldcupexplorer.presentation.common.UiState
import com.example.worldcupexplorer.presentation.components.AppBackground
import com.example.worldcupexplorer.presentation.components.AppTopBar
import com.example.worldcupexplorer.presentation.components.HeroBanner
import com.example.worldcupexplorer.presentation.components.ScreenStateContent

@Composable
fun ScorersScreen(
    uiState: UiState<List<Scorer>>,
    onRetry: () -> Unit
) {
    AppBackground {
        Scaffold(
            topBar = { AppTopBar(title = "Top Scorers") },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { innerPadding ->
            ScreenStateContent(
                state = uiState,
                onRetry = onRetry,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) { scorers ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        HeroBanner(
                            title = "Top scorers",
                            subtitle = "The players putting the ball in the net",
                            detail = "${scorers.size} scorers tracked"
                        )
                    }
                    items(scorers, key = { it.rank }) { scorer ->
                        ScorerCard(scorer = scorer)
                    }
                }
            }
        }
    }
}

@Composable
private fun ScorerCard(scorer: Scorer) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = "#${scorer.rank}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.tertiary)
            Text(text = scorer.playerName, style = MaterialTheme.typography.titleMedium)
            Text(text = "Team: ${scorer.team.name}")
            Text(text = "Goals: ${scorer.goals}", style = MaterialTheme.typography.headlineSmall)
        }
    }
}
