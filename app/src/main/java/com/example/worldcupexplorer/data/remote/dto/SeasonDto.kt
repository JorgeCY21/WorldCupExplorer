package com.example.worldcupexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SeasonDto(
    @SerializedName("startDate")
    val startDate: String? = null,
    @SerializedName("endDate")
    val endDate: String? = null,
    @SerializedName("currentMatchday")
    val currentMatchday: Int? = null,
    @SerializedName("winner")
    val winner: TeamDto? = null
)
