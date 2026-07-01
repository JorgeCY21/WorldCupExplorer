package com.example.worldcupexplorer.data.local.mapper

import com.example.worldcupexplorer.data.local.entity.CompetitionEntity
import com.example.worldcupexplorer.data.local.entity.MatchEntity
import com.example.worldcupexplorer.data.local.entity.PlayerEntity
import com.example.worldcupexplorer.data.local.entity.ScorerEntity
import com.example.worldcupexplorer.data.local.entity.StandingEntryEntity
import com.example.worldcupexplorer.data.local.entity.StandingGroupEntity
import com.example.worldcupexplorer.data.local.entity.TeamEntity
import com.example.worldcupexplorer.data.remote.dto.CompetitionResponseDto
import com.example.worldcupexplorer.data.remote.dto.MatchDto
import com.example.worldcupexplorer.data.remote.dto.PlayerDto
import com.example.worldcupexplorer.data.remote.dto.ScorerDto
import com.example.worldcupexplorer.data.remote.dto.ScoreDto
import com.example.worldcupexplorer.data.remote.dto.StandingDto
import com.example.worldcupexplorer.data.remote.dto.StandingsTableDto
import com.example.worldcupexplorer.data.remote.dto.TeamDto
import com.example.worldcupexplorer.domain.model.Competition
import com.example.worldcupexplorer.domain.model.Match
import com.example.worldcupexplorer.domain.model.MatchTeam
import com.example.worldcupexplorer.domain.model.Player
import com.example.worldcupexplorer.domain.model.Scorer
import com.example.worldcupexplorer.domain.model.SeasonInfo
import com.example.worldcupexplorer.domain.model.StandingEntry
import com.example.worldcupexplorer.domain.model.StandingGroup
import com.example.worldcupexplorer.domain.model.Team

fun CompetitionResponseDto.toEntity(): CompetitionEntity {
    return CompetitionEntity(
        id = id,
        name = name,
        code = code.orEmpty(),
        type = type.orEmpty(),
        emblemUrl = emblem,
        areaName = area?.name,
        seasonStartDate = currentSeason?.startDate,
        seasonEndDate = currentSeason?.endDate,
        currentMatchday = currentSeason?.currentMatchday,
        winnerName = currentSeason?.winner?.name,
        numberOfAvailableMatches = seasons.size
    )
}

fun CompetitionEntity.toDomain(): Competition {
    return Competition(
        id = id,
        name = name,
        code = code,
        type = type,
        emblemUrl = emblemUrl,
        areaName = areaName,
        currentSeason = SeasonInfo(
            startDate = seasonStartDate,
            endDate = seasonEndDate,
            currentMatchday = currentMatchday,
            winnerName = winnerName
        ),
        numberOfAvailableMatches = numberOfAvailableMatches
    )
}

fun TeamDto.toEntities(): Pair<TeamEntity, List<PlayerEntity>> {
    val teamEntity = TeamEntity(
        id = id,
        name = name,
        shortName = shortName,
        tla = tla,
        countryCode = area?.code,
        flagUrl = area?.flag,
        crestUrl = crest,
        coachName = coach?.name,
        country = area?.name,
        venue = venue,
        website = website,
        address = address,
        clubColors = clubColors
    )
    val playerEntities = squad.mapNotNull { player -> player.toEntity(teamId = id) }
    return teamEntity to playerEntities
}

fun PlayerDto.toEntity(teamId: Int): PlayerEntity? {
    val playerName = name ?: return null
    return PlayerEntity(
        teamId = teamId,
        remotePlayerId = id,
        name = playerName,
        position = position,
        nationality = nationality,
        dateOfBirth = dateOfBirth
    )
}

fun TeamEntity.toDomain(players: List<PlayerEntity> = emptyList()): Team {
    return Team(
        id = id,
        name = name,
        shortName = shortName,
        tla = tla,
        countryCode = countryCode,
        flagUrl = flagUrl,
        crestUrl = crestUrl,
        coachName = coachName,
        country = country,
        venue = venue,
        website = website,
        address = address,
        clubColors = clubColors,
        squad = players.map { it.toDomain() }
    )
}

fun PlayerEntity.toDomain(): Player {
    return Player(
        id = remotePlayerId,
        name = name,
        position = position,
        nationality = nationality,
        dateOfBirth = dateOfBirth
    )
}

fun MatchDto.toEntity(): MatchEntity {
    return MatchEntity(
        id = id,
        homeTeamId = homeTeam.id,
        homeTeamName = homeTeam.name.orEmpty(),
        homeTeamCountryCode = homeTeam.area?.code,
        homeTeamCrestUrl = homeTeam.crest,
        awayTeamId = awayTeam.id,
        awayTeamName = awayTeam.name.orEmpty(),
        awayTeamCountryCode = awayTeam.area?.code,
        awayTeamCrestUrl = awayTeam.crest,
        utcDate = utcDate,
        status = status,
        score = score.toDisplayText(),
        stage = stage,
        groupName = group
    )
}

fun MatchEntity.toDomain(): Match {
    return Match(
        id = id,
        homeTeam = MatchTeam(
            id = homeTeamId,
            name = homeTeamName,
            countryCode = homeTeamCountryCode,
            crestUrl = homeTeamCrestUrl
        ),
        awayTeam = MatchTeam(
            id = awayTeamId,
            name = awayTeamName,
            countryCode = awayTeamCountryCode,
            crestUrl = awayTeamCrestUrl
        ),
        utcDate = utcDate,
        status = status,
        score = score,
        stage = stage,
        group = groupName
    )
}

fun StandingsTableDto.toEntities(): Pair<StandingGroupEntity, List<StandingEntryEntity>> {
    val groupKey = buildStandingGroupKey(group = group, stage = stage, type = type)
    val groupEntity = StandingGroupEntity(
        groupKey = groupKey,
        groupName = group,
        stage = stage,
        type = type
    )
    val entryEntities = table.map { it.toEntity(groupKey) }
    return groupEntity to entryEntities
}

fun StandingDto.toEntity(groupKey: String): StandingEntryEntity {
    return StandingEntryEntity(
        groupKey = groupKey,
        position = position,
        teamId = team?.id ?: 0,
        teamName = team?.name.orEmpty(),
        countryCode = team?.area?.code,
        crestUrl = team?.crest,
        playedGames = playedGames,
        won = won,
        draw = draw,
        lost = lost,
        goalDifference = goalDifference,
        points = points
    )
}

fun StandingGroupEntity.toDomain(entries: List<StandingEntryEntity>): StandingGroup {
    return StandingGroup(
        group = groupName,
        stage = stage,
        type = type,
        table = entries.sortedBy { it.position }.map { it.toDomain() }
    )
}

fun StandingEntryEntity.toDomain(): StandingEntry {
    return StandingEntry(
        position = position,
        team = MatchTeam(
            id = teamId,
            name = teamName,
            countryCode = countryCode,
            crestUrl = crestUrl
        ),
        playedGames = playedGames,
        won = won,
        draw = draw,
        lost = lost,
        goalDifference = goalDifference,
        points = points
    )
}

fun ScorerDto.toEntity(rank: Int): ScorerEntity {
    return ScorerEntity(
        rank = rank,
        playerName = player?.name.orEmpty(),
        teamId = team?.id ?: 0,
        teamName = team?.name.orEmpty(),
        countryCode = team?.area?.code,
        crestUrl = team?.crest,
        goals = goals
    )
}

fun ScorerEntity.toDomain(): Scorer {
    return Scorer(
        rank = rank,
        playerName = playerName,
        team = MatchTeam(
            id = teamId,
            name = teamName,
            countryCode = countryCode,
            crestUrl = crestUrl
        ),
        goals = goals
    )
}

private fun ScoreDto?.toDisplayText(): String? {
    if (this == null) return null
    val fullTime = fullTime
    return when {
        fullTime?.home != null || fullTime?.away != null -> "${fullTime.home ?: "-"} - ${fullTime.away ?: "-"}"
        halfTime?.home != null || halfTime?.away != null -> "${halfTime.home ?: "-"} - ${halfTime.away ?: "-"}"
        else -> "vs"
    }
}

private fun buildStandingGroupKey(
    group: String?,
    stage: String?,
    type: String?
): String {
    return listOfNotNull(stage, type, group).joinToString("|").ifBlank {
        group ?: stage ?: type ?: "default"
    }
}
