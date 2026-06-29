package com.example.worldcupexplorer.data.repository

import com.example.worldcupexplorer.data.remote.mapper.toDomain
import com.example.worldcupexplorer.data.remote.source.FootballRemoteDataSource
import com.example.worldcupexplorer.domain.model.Competition
import com.example.worldcupexplorer.domain.model.HomeDashboard
import com.example.worldcupexplorer.domain.model.Match
import com.example.worldcupexplorer.domain.model.Scorer
import com.example.worldcupexplorer.domain.model.StandingGroup
import com.example.worldcupexplorer.domain.model.Team
import com.example.worldcupexplorer.domain.repository.FootballRepository
import java.io.IOException
import java.util.concurrent.CancellationException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class FootballRepositoryImpl @Inject constructor(
    private val remoteDataSource: FootballRemoteDataSource
) : FootballRepository {

    override fun getCompetition(): Flow<Result<Competition>> = safeFlow {
        remoteDataSource.getWorldCupCompetition().toDomain()
    }

    override fun getCompetitions(): Flow<Result<List<Competition>>> = safeFlow {
        remoteDataSource.getCompetitions().map { it.toDomain() }
    }

    override fun getTeams(): Flow<Result<List<Team>>> = safeFlow {
        remoteDataSource.getWorldCupTeams().teams.map { it.toDomain() }
    }

    override fun getTeamDetails(teamId: Int): Flow<Result<Team>> = safeFlow {
        remoteDataSource.getTeamDetails(teamId).toDomain()
    }

    override fun getMatches(): Flow<Result<List<Match>>> = safeFlow {
        remoteDataSource.getWorldCupMatches().matches.map { it.toDomain() }
    }

    override fun getStandings(): Flow<Result<List<StandingGroup>>> = safeFlow {
        remoteDataSource.getWorldCupStandings().standings.map { it.toDomain() }
    }

    override fun getScorers(): Flow<Result<List<Scorer>>> = safeFlow {
        remoteDataSource.getWorldCupScorers().scorers.mapIndexed { index, scorer ->
            scorer.toDomain(index + 1)
        }
    }

    override fun getHomeDashboard(): Flow<Result<HomeDashboard>> = safeFlow {
        val competition = remoteDataSource.getWorldCupCompetition().toDomain()
        val teamsCount = remoteDataSource.getWorldCupTeams().teams.size
        val matchesCount = remoteDataSource.getWorldCupMatches().matches.size
        val scorersCount = remoteDataSource.getWorldCupScorers().scorers.size
        val groupsCount = remoteDataSource.getWorldCupStandings().standings.size

        HomeDashboard(
            competition = competition,
            teamsCount = teamsCount,
            matchesCount = matchesCount,
            scorersCount = scorersCount,
            groupsCount = groupsCount
        )
    }

    private inline fun <T> safeFlow(crossinline block: suspend () -> T): Flow<Result<T>> {
        return flow {
            emit(Result.success(block()))
        }.catch { throwable ->
            if (throwable is CancellationException) {
                throw throwable
            }
            emit(Result.failure(throwable.toFriendlyException()))
        }
    }

    private fun Throwable.toFriendlyException(): Throwable {
        return when (this) {
            is IOException -> IOException("Please check your internet connection and try again.", this)
            else -> RuntimeException(
                message ?: "Something went wrong while loading football data.",
                this
            )
        }
    }
}
