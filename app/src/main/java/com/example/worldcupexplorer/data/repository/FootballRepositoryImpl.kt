package com.example.worldcupexplorer.data.repository

import androidx.room.withTransaction
import com.example.worldcupexplorer.data.local.db.FootballDatabase
import com.example.worldcupexplorer.data.local.mapper.toDomain
import com.example.worldcupexplorer.data.local.mapper.toEntities
import com.example.worldcupexplorer.data.local.mapper.toEntity
import com.example.worldcupexplorer.data.network.NetworkMonitor
import com.example.worldcupexplorer.data.remote.source.FootballRemoteDataSource
import com.example.worldcupexplorer.di.ApplicationScope
import com.example.worldcupexplorer.domain.model.Competition
import com.example.worldcupexplorer.domain.model.HomeDashboard
import com.example.worldcupexplorer.domain.model.Match
import com.example.worldcupexplorer.domain.model.Scorer
import com.example.worldcupexplorer.domain.model.StandingGroup
import com.example.worldcupexplorer.domain.model.Team
import com.example.worldcupexplorer.domain.repository.FootballRepository
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.CancellationException
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.HttpException

class FootballRepositoryImpl @Inject constructor(
    private val database: FootballDatabase,
    private val remoteDataSource: FootballRemoteDataSource,
    private val networkMonitor: NetworkMonitor,
    @ApplicationScope private val appScope: CoroutineScope
) : FootballRepository {

    private val syncMutex = Mutex()

    init {
        appScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .filter { it }
                .collect {
                    refreshAllCachedData()
                }
        }
    }

    override fun getCompetition(): Flow<Result<Competition>> {
        return observeRoomFlow(database.competitionDao().observeCompetition()) { competition ->
            competition?.toDomain() ?: defaultCompetition()
        }
    }

    override fun getCompetitions(): Flow<Result<List<Competition>>> {
        return observeRoomFlow(database.competitionDao().observeCompetition()) { competition ->
            listOf(competition?.toDomain() ?: defaultCompetition())
        }
    }

    override fun getTeams(): Flow<Result<List<Team>>> {
        return observeRoomFlow(database.teamDao().observeTeams()) { teams ->
            teams.map { it.toDomain() }
        }
    }

    override fun getTeamDetails(teamId: Int): Flow<Result<Team>> {
        return observeRoomFlow(
            combine(
                database.teamDao().observeTeam(teamId),
                database.teamDao().observePlayers(teamId)
            ) { team, players ->
                team?.toDomain(players) ?: defaultTeam(teamId)
            }
        ) { it }
    }

    override fun getMatches(): Flow<Result<List<Match>>> {
        return observeRoomFlow(database.matchDao().observeMatches()) { matches ->
            matches.map { it.toDomain() }
        }
    }

    override fun getStandings(): Flow<Result<List<StandingGroup>>> {
        return observeRoomFlow(
            combine(
                database.standingDao().observeGroups(),
                database.standingDao().observeEntries()
            ) { groups, entries ->
                groups.map { group ->
                    group.toDomain(entries.filter { it.groupKey == group.groupKey })
                }
            }
        ) { it }
    }

    override fun getScorers(): Flow<Result<List<Scorer>>> {
        return observeRoomFlow(database.scorerDao().observeScorers()) { scorers ->
            scorers.map { it.toDomain() }
        }
    }

    override fun getHomeDashboard(): Flow<Result<HomeDashboard>> {
        return observeRoomFlow(
            combine(
                database.competitionDao().observeCompetition(),
                database.teamDao().observeTeams(),
                database.matchDao().observeMatches(),
                database.scorerDao().observeScorers(),
                combine(
                    database.standingDao().observeGroups(),
                    database.standingDao().observeEntries()
                ) { groups, entries ->
                    groups.map { group ->
                        group.toDomain(entries.filter { it.groupKey == group.groupKey })
                    }
                }
            ) { competition, teams, matches, scorers, standings ->
                HomeDashboard(
                    competition = competition?.toDomain() ?: defaultCompetition(),
                    teamsCount = teams.size,
                    matchesCount = matches.size,
                    scorersCount = scorers.size,
                    groupsCount = standings.size
                )
            }
        ) { it }
    }

    private suspend fun refreshAllCachedData() {
        syncMutex.withLock {
            runCatching {
                val competitionResponse = remoteDataSource.getWorldCupCompetition()
                val teamsResponse = remoteDataSource.getWorldCupTeams()
                val matchesResponse = remoteDataSource.getWorldCupMatches()
                val standingsResponse = remoteDataSource.getWorldCupStandings()
                val scorersResponse = remoteDataSource.getWorldCupScorers()

                val competitionEntity = competitionResponse.toEntity()
                val teamBundles = teamsResponse.teams.map { it.toEntities() }
                val teamEntities = teamBundles.map { it.first }
                val playerEntities = teamBundles.flatMap { it.second }
                val matchEntities = matchesResponse.matches.map { it.toEntity() }
                val standingBundles = standingsResponse.standings.map { it.toEntities() }
                val standingGroups = standingBundles.map { it.first }
                val standingEntries = standingBundles.flatMap { it.second }
                val scorerEntities = scorersResponse.scorers.mapIndexed { index, scorerDto ->
                    scorerDto.toEntity(index + 1)
                }

                database.withTransaction {
                    database.competitionDao().clearCompetition()
                    database.competitionDao().insertCompetition(competitionEntity)

                    database.teamDao().clearPlayers()
                    database.teamDao().clearTeams()
                    database.teamDao().insertTeams(teamEntities)
                    database.teamDao().insertPlayers(playerEntities)

                    database.matchDao().clearMatches()
                    database.matchDao().insertMatches(matchEntities)

                    database.standingDao().clearEntries()
                    database.standingDao().clearGroups()
                    database.standingDao().insertGroups(standingGroups)
                    database.standingDao().insertEntries(standingEntries)

                    database.scorerDao().clearScorers()
                    database.scorerDao().insertScorers(scorerEntities)
                }
            }
        }
    }

    private inline fun <T, R> observeRoomFlow(
        source: Flow<T>,
        crossinline mapper: (T) -> R
    ): Flow<Result<R>> {
        return source
            .map { value -> Result.success(mapper(value)) }
            .catch { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }
                emit(Result.failure(throwable.toFriendlyException()))
            }
    }

    private fun defaultCompetition(): Competition {
        return Competition(
            id = 0,
            name = "World Cup Explorer",
            code = "WC",
            type = "TOURNAMENT",
            emblemUrl = null,
            areaName = "FIFA World Cup",
            currentSeason = null,
            numberOfAvailableMatches = 0
        )
    }

    private fun defaultTeam(teamId: Int): Team {
        return Team(
            id = teamId,
            name = "Team $teamId"
        )
    }

    private fun Throwable.toFriendlyException(): Throwable {
        return when (this) {
            is SocketTimeoutException -> IOException(
                "The request timed out. Please try again.",
                this
            )

            is HttpException -> RuntimeException(
                "Server error while syncing football data.",
                this
            )

            else -> when (this) {
                is IOException -> IOException(
                    "Please check your internet connection and try again.",
                    this
                )

                else -> RuntimeException(
                    message ?: "Something went wrong while loading football data.",
                    this
                )
            }
        }
    }
}
