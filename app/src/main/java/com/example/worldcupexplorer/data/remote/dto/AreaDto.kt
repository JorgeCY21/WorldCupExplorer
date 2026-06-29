package com.example.worldcupexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AreaDto(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("flag")
    val flag: String? = null
)
