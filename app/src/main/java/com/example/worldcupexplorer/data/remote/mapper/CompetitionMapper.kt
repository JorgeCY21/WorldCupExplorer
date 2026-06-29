package com.example.worldcupexplorer.data.remote.mapper

import com.example.worldcupexplorer.data.remote.dto.CompetitionResponseDto
import com.example.worldcupexplorer.data.remote.dto.CompetitionDto
import com.example.worldcupexplorer.data.remote.dto.SeasonDto
import com.example.worldcupexplorer.domain.model.Competition
import com.example.worldcupexplorer.domain.model.SeasonInfo

fun CompetitionDto.toDomain(): Competition {
    return Competition(
        id = id,
        name = name,
        code = code.orEmpty(),
        type = type.orEmpty(),
        emblemUrl = emblem
    )
}

fun CompetitionResponseDto.toDomain(): Competition {
    return Competition(
        id = id,
        name = name,
        code = code.orEmpty(),
        type = type.orEmpty(),
        emblemUrl = emblem,
        areaName = area?.name,
        currentSeason = currentSeason?.toDomain(),
        numberOfAvailableMatches = seasons.size
    )
}

fun SeasonDto.toDomain(): SeasonInfo {
    return SeasonInfo(
        startDate = startDate,
        endDate = endDate,
        currentMatchday = currentMatchday,
        winnerName = winner?.name
    )
}
