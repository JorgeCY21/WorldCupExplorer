package com.example.worldcupexplorer.domain.model

data class HomeDashboard(
    val competition: Competition,
    val teamsCount: Int,
    val matchesCount: Int,
    val scorersCount: Int,
    val groupsCount: Int
)
