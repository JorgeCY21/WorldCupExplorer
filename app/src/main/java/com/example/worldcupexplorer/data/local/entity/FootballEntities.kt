package com.example.worldcupexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "competition_cache")
data class CompetitionEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val code: String,
    val type: String,
    val emblemUrl: String? = null,
    val areaName: String? = null,
    val seasonStartDate: String? = null,
    val seasonEndDate: String? = null,
    val currentMatchday: Int? = null,
    val winnerName: String? = null,
    val numberOfAvailableMatches: Int? = null
)

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val shortName: String? = null,
    val tla: String? = null,
    val countryCode: String? = null,
    val flagUrl: String? = null,
    val crestUrl: String? = null,
    val coachName: String? = null,
    val country: String? = null,
    val venue: String? = null,
    val website: String? = null,
    val address: String? = null,
    val clubColors: String? = null
)

@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) val rowId: Long = 0L,
    val teamId: Int,
    val remotePlayerId: Int? = null,
    val name: String,
    val position: String? = null,
    val nationality: String? = null,
    val dateOfBirth: String? = null
)

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey val id: Int,
    val homeTeamId: Int,
    val homeTeamName: String,
    val homeTeamCountryCode: String? = null,
    val homeTeamCrestUrl: String? = null,
    val awayTeamId: Int,
    val awayTeamName: String,
    val awayTeamCountryCode: String? = null,
    val awayTeamCrestUrl: String? = null,
    val utcDate: String? = null,
    val status: String? = null,
    val score: String? = null,
    val stage: String? = null,
    val groupName: String? = null
)

@Entity(tableName = "standing_groups")
data class StandingGroupEntity(
    @PrimaryKey val groupKey: String,
    val groupName: String? = null,
    val stage: String? = null,
    val type: String? = null
)

@Entity(tableName = "standing_entries")
data class StandingEntryEntity(
    @PrimaryKey(autoGenerate = true) val rowId: Long = 0L,
    val groupKey: String,
    val position: Int,
    val teamId: Int,
    val teamName: String,
    val countryCode: String? = null,
    val crestUrl: String? = null,
    val playedGames: Int,
    val won: Int,
    val draw: Int,
    val lost: Int,
    val goalDifference: Int,
    val points: Int
)

@Entity(tableName = "scorers")
data class ScorerEntity(
    @PrimaryKey val rank: Int,
    val playerName: String,
    val teamId: Int,
    val teamName: String,
    val countryCode: String? = null,
    val crestUrl: String? = null,
    val goals: Int
)
