package com.example.worldcupexplorer.data.remote.api

import com.example.worldcupexplorer.data.remote.dto.CompetitionResponseDto
import com.example.worldcupexplorer.data.remote.dto.MatchesResponseDto
import com.example.worldcupexplorer.data.remote.dto.ScorersResponseDto
import com.example.worldcupexplorer.data.remote.dto.StandingsResponseDto
import com.example.worldcupexplorer.data.remote.dto.CompetitionsResponseDto
import com.example.worldcupexplorer.data.remote.dto.TeamDto
import com.example.worldcupexplorer.data.remote.dto.TeamsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FootballDataApiService {
    @GET("competitions")
    suspend fun getCompetitions(): CompetitionsResponseDto

    @GET("competitions/WC")
    suspend fun getWorldCupCompetition(): CompetitionResponseDto

    @GET("competitions/WC/teams")
    suspend fun getWorldCupTeams(): TeamsResponseDto

    @GET("teams/{id}")
    suspend fun getTeamDetails(@Path("id") teamId: Int): TeamDto

    @GET("competitions/WC/matches")
    suspend fun getWorldCupMatches(): MatchesResponseDto

    @GET("competitions/WC/standings")
    suspend fun getWorldCupStandings(): StandingsResponseDto

    @GET("competitions/WC/scorers")
    suspend fun getWorldCupScorers(): ScorersResponseDto
}
