package com.example.worldcupexplorer.presentation.about

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
import com.example.worldcupexplorer.presentation.common.UiState
import com.example.worldcupexplorer.presentation.components.AppBackground
import com.example.worldcupexplorer.presentation.components.AppTopBar
import com.example.worldcupexplorer.presentation.components.HeroBanner
import com.example.worldcupexplorer.presentation.components.ScreenStateContent

@Composable
fun AboutScreen(
    uiState: UiState<AboutUiModel>,
    onRetry: () -> Unit
) {
    AppBackground {
        Scaffold(
            topBar = { AppTopBar(title = "About") },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { innerPadding ->
            ScreenStateContent(
                state = uiState,
                onRetry = onRetry,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) { about ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        HeroBanner(
                            title = about.appName,
                            subtitle = "A polished World Cup explorer built for the tournament",
                            detail = about.note
                        )
                    }
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                            colors = androidx.compose.material3.CardDefaults.cardColors(
                                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = "API", style = MaterialTheme.typography.titleMedium)
                                Text(text = about.apiName)
                                Text(text = about.apiCredit)
                            }
                        }
                    }
                }
            }
        }
    }
}
