package com.example.worldcupexplorer.presentation.components

fun String?.toDisplayDate(): String {
    if (this.isNullOrBlank()) return "N/A"
    return substringBefore("T").ifBlank { this }
}

fun String?.toDisplayTime(): String {
    if (this.isNullOrBlank()) return "N/A"
    val timePart = substringAfter("T", missingDelimiterValue = this)
    return timePart.take(5).ifBlank { this }
}

fun String?.toFriendlyLabel(): String {
    if (this.isNullOrBlank()) return "N/A"
    return replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }
}
