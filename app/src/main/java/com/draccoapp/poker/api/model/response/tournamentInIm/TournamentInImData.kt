package com.draccoapp.poker.api.model.response.tournamentInIm


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TournamentInImData(
    @Json(name = "id")
    val id: String?,
    @Json(name = "account")
    val account: Account?,
    @Json(name = "tournament")
    val tournament: Tournament?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "answers")
    val answers: List<Answer>?
)