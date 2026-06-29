package com.example.worldcupexplorer.presentation.teamdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldcupexplorer.domain.model.Team
import com.example.worldcupexplorer.domain.repository.FootballRepository
import com.example.worldcupexplorer.navigation.AppDestination
import com.example.worldcupexplorer.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TeamDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: FootballRepository
) : ViewModel() {

    private val teamId: Int = savedStateHandle.get<Int>(AppDestination.TeamDetailsArg) ?: 0

    private val _uiState = MutableStateFlow<UiState<Team>>(UiState.Loading)
    val uiState: StateFlow<UiState<Team>> = _uiState.asStateFlow()

    init {
        loadTeamDetails()
    }

    fun loadTeamDetails() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            if (teamId == 0) {
                _uiState.value = UiState.Error("Missing team id.")
                return@launch
            }
            repository.getTeamDetails(teamId).collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { UiState.Success(it) },
                    onFailure = { UiState.Error(it.message ?: "Unable to load team details.") }
                )
            }
        }
    }
}
