package com.example.worldcupexplorer.data.remote.mapper

import com.example.worldcupexplorer.data.remote.dto.MatchDto
import com.example.worldcupexplorer.data.remote.dto.PlayerDto
import com.example.worldcupexplorer.data.remote.dto.ScorerDto
import com.example.worldcupexplorer.data.remote.dto.ScoreDto
import com.example.worldcupexplorer.data.remote.dto.StandingDto
import com.example.worldcupexplorer.data.remote.dto.StandingsTableDto
import com.example.worldcupexplorer.data.remote.dto.TeamDto
import com.example.worldcupexplorer.domain.model.Match
import com.example.worldcupexplorer.domain.model.MatchTeam
import com.example.worldcupexplorer.domain.model.Player
import com.example.worldcupexplorer.domain.model.Scorer
import com.example.worldcupexplorer.domain.model.StandingEntry
import com.example.worldcupexplorer.domain.model.StandingGroup
import com.example.worldcupexplorer.domain.model.Team

fun TeamDto.toDomain(): Team {
    return Team(
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
        clubColors = clubColors,
        squad = squad.mapNotNull { it.toDomainOrNull() }
    )
}

fun PlayerDto.toDomainOrNull(): Player? {
    val playerName = name ?: return null
    return Player(
        id = id,
        name = playerName,
        position = position,
        nationality = nationality,
        dateOfBirth = dateOfBirth
    )
}

fun MatchDto.toDomain(): Match {
    return Match(
        id = id,
        homeTeam = MatchTeam(
            id = homeTeam.id,
            name = homeTeam.name.orEmpty(),
            crestUrl = homeTeam.crest
        ),
        awayTeam = MatchTeam(
            id = awayTeam.id,
            name = awayTeam.name.orEmpty(),
            crestUrl = awayTeam.crest
        ),
        utcDate = utcDate,
        status = status,
        score = score.toDisplayText(),
        stage = stage,
        group = group
    )
}

private fun ScoreDto?.toDisplayText(): String {
    if (this == null) return "vs"
    val fullTime = fullTime
    val regular = when {
        fullTime?.home != null || fullTime?.away != null -> "${fullTime.home ?: "-"} - ${fullTime.away ?: "-"}"
        halfTime?.home != null || halfTime?.away != null -> "${halfTime.home ?: "-"} - ${halfTime.away ?: "-"}"
        else -> "vs"
    }
    return regular
}

fun StandingsTableDto.toDomain(): StandingGroup {
    return StandingGroup(
        group = group,
        stage = stage,
        type = type,
        table = table.map { it.toDomain() }
    )
}

fun StandingDto.toDomain(): StandingEntry {
    return StandingEntry(
        position = position,
        team = MatchTeam(
            id = team?.id ?: 0,
            name = team?.name.orEmpty(),
            crestUrl = team?.crest
        ),
        playedGames = playedGames,
        won = won,
        draw = draw,
        lost = lost,
        goalDifference = goalDifference,
        points = points
    )
}

fun ScorerDto.toDomain(rank: Int): Scorer {
    return Scorer(
        rank = rank,
        playerName = player?.name.orEmpty(),
        team = MatchTeam(
            id = team?.id ?: 0,
            name = team?.name.orEmpty(),
            crestUrl = team?.crest
        ),
        goals = goals
    )
}
