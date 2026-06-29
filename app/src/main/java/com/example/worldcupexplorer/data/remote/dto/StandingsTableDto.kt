package com.example.worldcupexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class StandingsTableDto(
    @SerializedName("stage")
    val stage: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("group")
    val group: String? = null,
    @SerializedName("table")
    val table: List<StandingDto> = emptyList()
)
