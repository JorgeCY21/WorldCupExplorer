package com.example.worldcupexplorer.domain.model

data class Team(
    val id: Int,
    val name: String,
    val shortName: String? = null,
    val tla: String? = null,
    val countryCode: String? = null,
    val flagUrl: String? = null,
    val crestUrl: String? = null,
    val coachName: String? = null,
    val country: String? = null,
    val venue: String? = null,
    val website: String? = null,
    val address: String? = null,
    val clubColors: String? = null,
    val squad: List<Player> = emptyList()
)
