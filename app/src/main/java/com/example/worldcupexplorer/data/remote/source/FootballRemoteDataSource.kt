package com.example.worldcupexplorer.data.remote.source

import com.example.worldcupexplorer.data.remote.api.FootballDataApiService
import com.example.worldcupexplorer.data.remote.dto.CompetitionResponseDto
import com.example.worldcupexplorer.data.remote.dto.CompetitionDto
import com.example.worldcupexplorer.data.remote.dto.MatchesResponseDto
import com.example.worldcupexplorer.data.remote.dto.ScorersResponseDto
import com.example.worldcupexplorer.data.remote.dto.StandingsResponseDto
import com.example.worldcupexplorer.data.remote.dto.TeamDto
import com.example.worldcupexplorer.data.remote.dto.TeamsResponseDto
import javax.inject.Inject

class FootballRemoteDataSource @Inject constructor(
    private val apiService: FootballDataApiService
) {
    suspend fun getCompetitions(): List<CompetitionDto> {
        return apiService.getCompetitions().competitions
    }

    suspend fun getWorldCupCompetition(): CompetitionResponseDto {
        return apiService.getWorldCupCompetition()
    }

    suspend fun getWorldCupTeams(): TeamsResponseDto {
        return apiService.getWorldCupTeams()
    }

    suspend fun getTeamDetails(teamId: Int): TeamDto {
        return apiService.getTeamDetails(teamId)
    }

    suspend fun getWorldCupMatches(): MatchesResponseDto {
        return apiService.getWorldCupMatches()
    }

    suspend fun getWorldCupStandings(): StandingsResponseDto {
        return apiService.getWorldCupStandings()
    }

    suspend fun getWorldCupScorers(): ScorersResponseDto {
        return apiService.getWorldCupScorers()
    }
}
