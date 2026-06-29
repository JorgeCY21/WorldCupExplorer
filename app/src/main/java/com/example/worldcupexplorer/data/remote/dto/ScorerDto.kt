package com.example.worldcupexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ScorerDto(
    @SerializedName("player")
    val player: PlayerDto? = null,
    @SerializedName("team")
    val team: TeamDto? = null,
    @SerializedName("goals")
    val goals: Int = 0,
    @SerializedName("assists")
    val assists: Int? = null,
    @SerializedName("penalties")
    val penalties: Int? = null
)
