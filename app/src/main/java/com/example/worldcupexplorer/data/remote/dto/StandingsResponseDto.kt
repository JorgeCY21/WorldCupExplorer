package com.example.worldcupexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class StandingsResponseDto(
    @SerializedName("standings")
    val standings: List<StandingsTableDto> = emptyList()
)
