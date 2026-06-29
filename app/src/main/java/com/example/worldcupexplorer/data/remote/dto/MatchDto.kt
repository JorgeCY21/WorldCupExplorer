package com.example.worldcupexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MatchDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("utcDate")
    val utcDate: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("stage")
    val stage: String? = null,
    @SerializedName("group")
    val group: String? = null,
    @SerializedName("homeTeam")
    val homeTeam: TeamDto,
    @SerializedName("awayTeam")
    val awayTeam: TeamDto,
    @SerializedName("score")
    val score: ScoreDto? = null
)
