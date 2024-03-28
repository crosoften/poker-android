package com.draccoapp.poker.api.model.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Entry(
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "tournamentId")
    val tournamentId: Int,
    @Json(name = "responses")
    val responses: List<String>
)