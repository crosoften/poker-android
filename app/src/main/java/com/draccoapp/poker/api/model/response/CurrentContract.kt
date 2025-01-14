package com.draccoapp.poker.api.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentContract(
    @Json(name = "profit")
    val profit: Double,
    @Json(name = "ranking")
    val ranking: Int,
    @Json(name = "timeLeft")
    val timeLeft: Int,
    @Json(name = "tournamentsCount")
    val tournamentsCount: Int
)