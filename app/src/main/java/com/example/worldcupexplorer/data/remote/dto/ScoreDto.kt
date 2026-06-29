package com.example.worldcupexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ScoreDto(
    @SerializedName("winner")
    val winner: String? = null,
    @SerializedName("duration")
    val duration: String? = null,
    @SerializedName("fullTime")
    val fullTime: ScoreValueDto? = null,
    @SerializedName("halfTime")
    val halfTime: ScoreValueDto? = null,
    @SerializedName("extraTime")
    val extraTime: ScoreValueDto? = null,
    @SerializedName("penalties")
    val penalties: ScoreValueDto? = null
)
