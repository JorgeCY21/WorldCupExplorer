package com.example.worldcupexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PlayerDto(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("position")
    val position: String? = null,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String? = null,
    @SerializedName("nationality")
    val nationality: String? = null
)
