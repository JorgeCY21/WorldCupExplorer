package com.example.worldcupexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CompetitionDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("area")
    val area: AreaDto? = null,
    @SerializedName("name")
    val name: String,
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("emblem")
    val emblem: String? = null
)
