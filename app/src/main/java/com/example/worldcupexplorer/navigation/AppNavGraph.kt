package com.example.worldcupexplorer.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.worldcupexplorer.presentation.about.AboutScreen
import com.example.worldcupexplorer.presentation.about.AboutViewModel
import com.example.worldcupexplorer.presentation.common.ConnectivityViewModel
import com.example.worldcupexplorer.presentation.common.LocalConnectivityStatus
import com.example.worldcupexplorer.presentation.home.HomeScreen
import com.example.worldcupexplorer.presentation.home.HomeViewModel
import com.example.worldcupexplorer.presentation.notifications.NotificationsViewModel
import com.example.worldcupexplorer.presentation.notifications.PushMessageBanner
import com.example.worldcupexplorer.presentation.matches.MatchesScreen
import com.example.worldcupexplorer.presentation.matches.MatchesViewModel
import com.example.worldcupexplorer.presentation.scorers.ScorersScreen
import com.example.worldcupexplorer.presentation.scorers.ScorersViewModel
import com.example.worldcupexplorer.presentation.standings.StandingsScreen
import com.example.worldcupexplorer.presentation.standings.StandingsViewModel
import com.example.worldcupexplorer.presentation.teamdetails.TeamDetailsScreen
import com.example.worldcupexplorer.presentation.teamdetails.TeamDetailsViewModel
import com.example.worldcupexplorer.presentation.teams.TeamsScreen
import com.example.worldcupexplorer.presentation.teams.TeamsViewModel

@Composable
fun AppNavGraph(
    pendingTeamId: Int? = null,
    onPendingTeamIdConsumed: () -> Unit = {}
) {
    val navController = rememberNavController()
    val connectivityViewModel: ConnectivityViewModel = hiltViewModel()
    val isOnline = connectivityViewModel.isOnline.collectAsStateWithLifecycle().value
    val notificationsViewModel: NotificationsViewModel = hiltViewModel()
    val lastPushMessage = notificationsViewModel.lastMessage.collectAsStateWithLifecycle().value

    LaunchedEffect(pendingTeamId) {
        if (pendingTeamId != null) {
            navController.navigate(AppDestination.teamDetailsRoute(pendingTeamId))
            onPendingTeamIdConsumed()
        }
    }

    CompositionLocalProvider(LocalConnectivityStatus provides isOnline) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = AppDestination.HomeRoute
            ) {
                composable(AppDestination.HomeRoute) {
                    val viewModel: HomeViewModel = hiltViewModel()
                    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
                    HomeScreen(
                        uiState = uiState,
                        onRetry = viewModel::loadHome,
                        onTeamsClick = { navController.navigate(AppDestination.TeamsRoute) },
                        onMatchesClick = { navController.navigate(AppDestination.MatchesRoute) },
                        onStandingsClick = { navController.navigate(AppDestination.StandingsRoute) },
                        onScorersClick = { navController.navigate(AppDestination.ScorersRoute) },
                        onAboutClick = { navController.navigate(AppDestination.AboutRoute) }
                    )
                }
                composable(AppDestination.TeamsRoute) {
                    val viewModel: TeamsViewModel = hiltViewModel()
                    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
                    TeamsScreen(
                        uiState = uiState,
                        onRetry = viewModel::loadTeams,
                        onTeamClick = { teamId -> navController.navigate(AppDestination.teamDetailsRoute(teamId)) }
                    )
                }
                composable(
                    route = "${AppDestination.TeamDetailsRoute}/{${AppDestination.TeamDetailsArg}}",
                    arguments = listOf(
                        navArgument(AppDestination.TeamDetailsArg) {
                            type = NavType.IntType
                        }
                    )
                ) {
                    val viewModel: TeamDetailsViewModel = hiltViewModel()
                    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
                    TeamDetailsScreen(
                        uiState = uiState,
                        onRetry = viewModel::loadTeamDetails,
                        onBackClick = { navController.popBackStack() }
                    )
                }
                composable(AppDestination.MatchesRoute) {
                    val viewModel: MatchesViewModel = hiltViewModel()
                    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
                    MatchesScreen(
                        uiState = uiState,
                        onRetry = viewModel::loadMatches
                    )
                }
                composable(AppDestination.StandingsRoute) {
                    val viewModel: StandingsViewModel = hiltViewModel()
                    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
                    StandingsScreen(
                        uiState = uiState,
                        onRetry = viewModel::loadStandings
                    )
                }
                composable(AppDestination.ScorersRoute) {
                    val viewModel: ScorersViewModel = hiltViewModel()
                    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
                    ScorersScreen(
                        uiState = uiState,
                        onRetry = viewModel::loadScorers
                    )
                }
                composable(AppDestination.AboutRoute) {
                    val viewModel: AboutViewModel = hiltViewModel()
                    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
                    AboutScreen(
                        uiState = uiState,
                        onRetry = viewModel::loadAbout
                    )
                }
            }
            if (lastPushMessage != null) {
                PushMessageBanner(
                    message = lastPushMessage,
                    onOpenTeam = { teamId ->
                        notificationsViewModel.dismissMessage()
                        navController.navigate(AppDestination.teamDetailsRoute(teamId))
                    },
                    onDismiss = notificationsViewModel::dismissMessage
                )
            }
        }
    }
}
