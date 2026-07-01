package com.example.worldcupexplorer.presentation.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldcupexplorer.domain.model.Match
import com.example.worldcupexplorer.domain.repository.FootballRepository
import com.example.worldcupexplorer.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val repository: FootballRepository
) : ViewModel() {

    private var loadJob: Job? = null
    private val _uiState = MutableStateFlow<UiState<List<Match>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Match>>> = _uiState.asStateFlow()

    init {
        loadMatches()
    }

    fun loadMatches() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getMatches().collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { UiState.Success(it) },
                    onFailure = { UiState.Error(it.message ?: "Unable to load matches.") }
                )
            }
        }
    }
}
