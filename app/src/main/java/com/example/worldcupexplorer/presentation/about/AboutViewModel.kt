package com.example.worldcupexplorer.presentation.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldcupexplorer.domain.repository.FootballRepository
import com.example.worldcupexplorer.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val repository: FootballRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<AboutUiModel>>(UiState.Loading)
    val uiState: StateFlow<UiState<AboutUiModel>> = _uiState.asStateFlow()

    init {
        loadAbout()
    }

    fun loadAbout() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getCompetition().collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { competition ->
                        UiState.Success(
                            AboutUiModel(
                                appName = "World Cup Explorer",
                                apiName = "football-data.org",
                                apiCredit = "Data provided by football-data.org",
                                note = "Current competition: ${competition.name}"
                            )
                        )
                    },
                    onFailure = {
                        UiState.Error(it.message ?: "Unable to load about information.")
                    }
                )
            }
        }
    }
}
