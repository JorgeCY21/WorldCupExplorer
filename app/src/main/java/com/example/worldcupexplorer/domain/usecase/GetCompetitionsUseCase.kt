package com.example.worldcupexplorer.domain.usecase

import com.example.worldcupexplorer.domain.model.Competition
import com.example.worldcupexplorer.domain.repository.FootballRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCompetitionsUseCase @Inject constructor(
    private val repository: FootballRepository
) {
    operator fun invoke(): Flow<Result<List<Competition>>> = repository.getCompetitions()
}
