package com.example.worldcupexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TeamDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("shortName")
    val shortName: String? = null,
    @SerializedName("tla")
    val tla: String? = null,
    @SerializedName("crest")
    val crest: String? = null,
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("website")
    val website: String? = null,
    @SerializedName("venue")
    val venue: String? = null,
    @SerializedName("clubColors")
    val clubColors: String? = null,
    @SerializedName("area")
    val area: AreaDto? = null,
    @SerializedName("coach")
    val coach: CoachDto? = null,
    @SerializedName("squad")
    val squad: List<PlayerDto> = emptyList()
)
