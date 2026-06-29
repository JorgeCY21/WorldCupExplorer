package com.example.worldcupexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ScorersResponseDto(
    @SerializedName("count")
    val count: Int? = null,
    @SerializedName("scorers")
    val scorers: List<ScorerDto> = emptyList()
)
