package com.example.worldcupexplorer.domain.model

data class Match(
    val id: Int,
    val homeTeam: MatchTeam,
    val awayTeam: MatchTeam,
    val utcDate: String? = null,
    val status: String? = null,
    val score: String? = null,
    val stage: String? = null,
    val group: String? = null
)
