package com.example.worldcupexplorer.domain.model

data class Player(
    val id: Int? = null,
    val name: String,
    val position: String? = null,
    val nationality: String? = null,
    val dateOfBirth: String? = null
)
