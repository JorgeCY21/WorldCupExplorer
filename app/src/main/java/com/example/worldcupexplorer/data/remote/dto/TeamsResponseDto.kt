package com.example.worldcupexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TeamsResponseDto(
    @SerializedName("count")
    val count: Int? = null,
    @SerializedName("filters")
    val filters: Map<String, String>? = null,
    @SerializedName("competition")
    val competition: CompetitionDto? = null,
    @SerializedName("season")
    val season: SeasonDto? = null,
    @SerializedName("teams")
    val teams: List<TeamDto> = emptyList()
)
