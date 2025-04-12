package com.draccoapp.poker.api.model.response.homeFrament


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OverallInfosX(
    @Json(name = "profit")
    val profit: Double,
    @Json(name = "ranking")
    val ranking: Int,
    @Json(name = "tournamentsCount")
    val tournamentsCount: Int
)