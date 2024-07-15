package com.draccoapp.poker.api.model.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TournamentBodyNew(
    @Json(name = "city")
    val city: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "eventUrl")
    val eventUrl: String,
    @Json(name = "prize")
    val prize: Int,
    @Json(name = "proofUrl")
    val proofUrl: String,
    @Json(name = "state")
    val state: String,
    @Json(name = "title")
    val title: String
)