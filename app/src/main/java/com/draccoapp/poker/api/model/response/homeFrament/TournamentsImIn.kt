package com.draccoapp.poker.api.model.response.homeFrament


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TournamentsImIn(
    @Json(name = "account")
    val account: Account,
    @Json(name = "answers")
    val answers: List<Any>,
    @Json(name = "id")
    val id: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "tournament")
    val tournament: Tournament
)