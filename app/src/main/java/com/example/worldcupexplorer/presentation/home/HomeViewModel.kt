package com.example.worldcupexplorer.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldcupexplorer.domain.model.HomeDashboard
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
class HomeViewModel @Inject constructor(
    private val repository: FootballRepository
) : ViewModel() {

    private var loadJob: Job? = null
    private val _uiState = MutableStateFlow<UiState<HomeDashboard>>(UiState.Loading)
    val uiState: StateFlow<UiState<HomeDashboard>> = _uiState.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getHomeDashboard().collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { UiState.Success(it) },
                    onFailure = { UiState.Error(it.message ?: "Unable to load World Cup data.") }
                )
            }
        }
    }
}
