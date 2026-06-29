package com.example.worldcupexplorer.domain.model

data class Scorer(
    val rank: Int,
    val playerName: String,
    val team: MatchTeam,
    val goals: Int
)
