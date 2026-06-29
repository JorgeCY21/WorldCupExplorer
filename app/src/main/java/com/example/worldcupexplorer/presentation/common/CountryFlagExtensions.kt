package com.example.worldcupexplorer.presentation.common

import java.util.Locale

private const val FLAGCDN_BASE_URL = "https://flagcdn.com/w80"

fun String?.toCountryFlagUrl(): String? {
    val rawValue = this
        ?.trim()
        ?.takeIf { it.isNotEmpty() }
        ?: return null

    val countryCode = rawValue
        .lowercase()
        .takeIf { it.length == 2 }
        ?: rawValue
            .uppercase()
            .takeIf { it.length == 3 }
            ?.toIsoAlpha2CountryCode()
        ?: rawValue.toCountryNameAlpha2Code()
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

private fun String.toCountryNameAlpha2Code(): String? {
    val normalized = normalizeCountryName()
    return COUNTRY_NAME_ALIASES[normalized]
}

private fun String.normalizeCountryName(): String {
    return lowercase()
        .replace("&", "and")
        .replace("-", " ")
        .replace(".", "")
        .replace(Regex("\\s+"), " ")
        .trim()
}

private val COUNTRY_NAME_ALIASES = mapOf(
    "algeria" to "dz",
    "argentina" to "ar",
    "australia" to "au",
    "austria" to "at",
    "belgium" to "be",
    "bosnia herzegovina" to "ba",
    "brazil" to "br",
    "canada" to "ca",
    "cape verde islands" to "cv",
    "colombia" to "co",
    "congo dr" to "cd",
    "croatia" to "hr",
    "curacao" to "cw",
    "czechia" to "cz",
    "ecuador" to "ec",
    "egypt" to "eg",
    "england" to "gb-eng",
    "france" to "fr",
    "germany" to "de",
    "ghana" to "gh",
    "haiti" to "ht",
    "iran" to "ir",
    "iraq" to "iq",
    "ivory coast" to "ci",
    "japan" to "jp",
    "jordan" to "jo",
    "mexico" to "mx",
    "morocco" to "ma",
    "netherlands" to "nl",
    "new zealand" to "nz",
    "norway" to "no",
    "panama" to "pa",
    "paraguay" to "py",
    "portugal" to "pt",
    "qatar" to "qa",
    "saudi arabia" to "sa",
    "scotland" to "gb-sct",
    "senegal" to "sn",
    "south africa" to "za",
    "south korea" to "kr",
    "spain" to "es",
    "sweden" to "se",
    "switzerland" to "ch",
    "tunisia" to "tn",
    "turkey" to "tr",
    "united states" to "us",
    "uruguay" to "uy",
    "uzbekistan" to "uz"
)
