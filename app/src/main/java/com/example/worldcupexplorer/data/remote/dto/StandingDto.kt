package com.example.worldcupexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class StandingDto(
    @SerializedName("position")
    val position: Int = 0,
    @SerializedName("team")
    val team: TeamDto? = null,
    @SerializedName("playedGames")
    val playedGames: Int = 0,
    @SerializedName("won")
    val won: Int = 0,
    @SerializedName("draw")
    val draw: Int = 0,
    @SerializedName("lost")
    val lost: Int = 0,
    @SerializedName("points")
    val points: Int = 0,
    @SerializedName("goalsFor")
    val goalsFor: Int = 0,
    @SerializedName("goalsAgainst")
    val goalsAgainst: Int = 0,
    @SerializedName("goalDifference")
    val goalDifference: Int = 0
)
