package com.example.worldcupexplorer.presentation.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldcupexplorer.domain.model.Team
import com.example.worldcupexplorer.domain.repository.FootballRepository
import com.example.worldcupexplorer.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TeamsViewModel @Inject constructor(
    private val repository: FootballRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Team>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Team>>> = _uiState.asStateFlow()

    init {
        loadTeams()
    }

    fun loadTeams() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getTeams().collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { UiState.Success(it) },
                    onFailure = { UiState.Error(it.message ?: "Unable to load teams.") }
                )
            }
        }
    }
}
