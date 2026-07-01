package com.example.worldcupexplorer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.worldcupexplorer.data.local.entity.CompetitionEntity
import com.example.worldcupexplorer.data.local.entity.MatchEntity
import com.example.worldcupexplorer.data.local.entity.PlayerEntity
import com.example.worldcupexplorer.data.local.entity.ScorerEntity
import com.example.worldcupexplorer.data.local.entity.StandingEntryEntity
import com.example.worldcupexplorer.data.local.entity.StandingGroupEntity
import com.example.worldcupexplorer.data.local.entity.TeamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompetitionDao {
    @Query("SELECT * FROM competition_cache LIMIT 1")
    fun observeCompetition(): Flow<CompetitionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompetition(competition: CompetitionEntity)

    @Query("DELETE FROM competition_cache")
    suspend fun clearCompetition()
}

@Dao
interface TeamDao {
    @Query("SELECT * FROM teams ORDER BY name ASC")
    fun observeTeams(): Flow<List<TeamEntity>>

    @Query("SELECT * FROM teams WHERE id = :teamId LIMIT 1")
    fun observeTeam(teamId: Int): Flow<TeamEntity?>

    @Query("SELECT * FROM players WHERE teamId = :teamId ORDER BY name ASC")
    fun observePlayers(teamId: Int): Flow<List<PlayerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayers(players: List<PlayerEntity>)

    @Query("DELETE FROM teams")
    suspend fun clearTeams()

    @Query("DELETE FROM players")
    suspend fun clearPlayers()
}

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches ORDER BY utcDate ASC")
    fun observeMatches(): Flow<List<MatchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(matches: List<MatchEntity>)

    @Query("DELETE FROM matches")
    suspend fun clearMatches()
}

@Dao
interface StandingDao {
    @Query("SELECT * FROM standing_groups ORDER BY groupName ASC, stage ASC")
    fun observeGroups(): Flow<List<StandingGroupEntity>>

    @Query("SELECT * FROM standing_entries ORDER BY position ASC")
    fun observeEntries(): Flow<List<StandingEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroups(groups: List<StandingGroupEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntries(entries: List<StandingEntryEntity>)

    @Query("DELETE FROM standing_groups")
    suspend fun clearGroups()

    @Query("DELETE FROM standing_entries")
    suspend fun clearEntries()
}

@Dao
interface ScorerDao {
    @Query("SELECT * FROM scorers ORDER BY rank ASC")
    fun observeScorers(): Flow<List<ScorerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScorers(scorers: List<ScorerEntity>)

    @Query("DELETE FROM scorers")
    suspend fun clearScorers()
}
