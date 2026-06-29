package com.example.worldcupexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MatchesResponseDto(
    @SerializedName("count")
    val count: Int? = null,
    @SerializedName("matches")
    val matches: List<MatchDto> = emptyList()
)
