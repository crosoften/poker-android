package com.draccoapp.poker.api.model.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApplicanteTournamentResponse(
    @Json(name = "totalNumberOfPages")
    val totalNumberOfPages: Int,
    @Json(name = "data")
    val applicantTournament: List<Data>
)