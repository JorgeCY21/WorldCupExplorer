package com.example.worldcupexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ScoreValueDto(
    @SerializedName("home")
    val home: Int? = null,
    @SerializedName("away")
    val away: Int? = null
)
