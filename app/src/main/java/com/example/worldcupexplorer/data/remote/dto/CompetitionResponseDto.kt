package com.example.worldcupexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CompetitionResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("area")
    val area: AreaDto? = null,
    @SerializedName("name")
    val name: String,
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("emblem")
    val emblem: String? = null,
    @SerializedName("currentSeason")
    val currentSeason: SeasonDto? = null,
    @SerializedName("seasons")
    val seasons: List<SeasonDto> = emptyList(),
    @SerializedName("lastUpdated")
    val lastUpdated: String? = null
)
