package com.example.worldcupexplorer.domain.model

data class StandingGroup(
    val group: String? = null,
    val stage: String? = null,
    val type: String? = null,
    val table: List<StandingEntry> = emptyList()
)
