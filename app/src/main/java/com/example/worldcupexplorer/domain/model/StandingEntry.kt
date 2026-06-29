package com.example.worldcupexplorer.domain.model

data class StandingEntry(
    val position: Int,
    val team: MatchTeam,
    val playedGames: Int,
    val won: Int,
    val draw: Int,
    val lost: Int,
    val goalDifference: Int,
    val points: Int
)
