package com.example.worldcupexplorer.presentation.scorers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldcupexplorer.domain.model.Scorer
import com.example.worldcupexplorer.domain.repository.FootballRepository
import com.example.worldcupexplorer.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ScorersViewModel @Inject constructor(
    private val repository: FootballRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Scorer>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Scorer>>> = _uiState.asStateFlow()

    init {
        loadScorers()
    }

    fun loadScorers() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getScorers().collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { UiState.Success(it) },
                    onFailure = { UiState.Error(it.message ?: "Unable to load scorers.") }
                )
            }
        }
    }
}
