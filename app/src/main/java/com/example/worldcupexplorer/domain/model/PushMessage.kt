package com.example.worldcupexplorer.domain.model

data class PushMessage(
    val title: String,
    val body: String,
    val teamId: Int? = null
)
