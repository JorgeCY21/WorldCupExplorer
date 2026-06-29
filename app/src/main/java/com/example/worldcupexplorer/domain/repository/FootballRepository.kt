package com.example.worldcupexplorer.domain.repository

import com.example.worldcupexplorer.domain.model.Competition
import com.example.worldcupexplorer.domain.model.HomeDashboard
import com.example.worldcupexplorer.domain.model.Match
import com.example.worldcupexplorer.domain.model.Scorer
import com.example.worldcupexplorer.domain.model.StandingGroup
import com.example.worldcupexplorer.domain.model.Team
import kotlinx.coroutines.flow.Flow

interface FootballRepository {
    fun getCompetition(): Flow<Result<Competition>>
    fun getCompetitions(): Flow<Result<List<Competition>>>
    fun getTeams(): Flow<Result<List<Team>>>
    fun getTeamDetails(teamId: Int): Flow<Result<Team>>
    fun getMatches(): Flow<Result<List<Match>>>
    fun getStandings(): Flow<Result<List<StandingGroup>>>
    fun getScorers(): Flow<Result<List<Scorer>>>
    fun getHomeDashboard(): Flow<Result<HomeDashboard>>
}
