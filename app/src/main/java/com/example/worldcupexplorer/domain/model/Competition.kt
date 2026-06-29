package com.example.worldcupexplorer.domain.model

data class Competition(
    val id: Int,
    val name: String,
    val code: String,
    val type: String,
    val emblemUrl: String?,
    val areaName: String? = null,
    val currentSeason: SeasonInfo? = null,
    val numberOfAvailableMatches: Int? = null
)
