package com.example.worldcupexplorer.presentation.common

import java.util.Locale

private const val FLAGCDN_BASE_URL = "https://flagcdn.com/w80"

fun String?.toCountryFlagUrl(): String? {
    val countryCode = this
        ?.trim()
        ?.lowercase()
        ?.takeIf { it.length == 2 }
        ?: this
            ?.trim()
            ?.uppercase()
            ?.takeIf { it.length == 3 }
            ?.toIsoAlpha2CountryCode()
        ?: return null

    return "$FLAGCDN_BASE_URL/$countryCode.png"
}

private fun String.toIsoAlpha2CountryCode(): String? {
    val alpha3 = uppercase()
    return Locale.getISOCountries()
        .asSequence()
        .firstOrNull { alpha2 ->
            runCatching { Locale("", alpha2).isO3Country == alpha3 }.getOrDefault(false)
        }
        ?.lowercase()
}
