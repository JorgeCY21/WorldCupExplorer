package com.example.worldcupexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CompetitionsResponseDto(
    @SerializedName("count")
    val count: Int? = null,
    @SerializedName("competitions")
    val competitions: List<CompetitionDto> = emptyList()
)
