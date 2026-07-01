package com.example.worldcupexplorer.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldcupexplorer.domain.model.Competition
import com.example.worldcupexplorer.domain.usecase.GetCompetitionsUseCase
import com.example.worldcupexplorer.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCompetitionsUseCase: GetCompetitionsUseCase
) : ViewModel() {

    private var loadJob: Job? = null
    private val _uiState = MutableStateFlow<UiState<List<Competition>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Competition>>> = _uiState.asStateFlow()

    init {
        loadCompetitions()
    }

    fun loadCompetitions() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = UiState.Loading
            getCompetitionsUseCase().collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { UiState.Success(it) },
                    onFailure = { UiState.Error(it.message ?: "Unknown error") }
                )
            }
        }
    }
}
