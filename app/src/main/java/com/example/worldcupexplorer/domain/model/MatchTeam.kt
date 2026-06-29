package com.example.worldcupexplorer.domain.model

data class MatchTeam(
    val id: Int,
    val name: String,
    val countryCode: String? = null,
    val crestUrl: String? = null
)
