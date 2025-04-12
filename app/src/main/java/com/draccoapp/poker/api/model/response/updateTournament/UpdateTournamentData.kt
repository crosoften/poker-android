package com.draccoapp.poker.api.model.response.updateTournament


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateTournamentData(
    @Json(name = "id")
    val id: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "message")
    val message: String?,
    @Json(name = "proofUrl")
    val proofUrl: String?,
    @Json(name = "date")
    val date: String?,
    @Json(name = "subscriptionId")
    val subscriptionId: String?
)