package com.draccoapp.poker.api.model.response.tournamentForms


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TournamentForms(
    @Json(name = "description")
    val description: String,
    @Json(name = "eventUrl")
    val eventUrl: String,
    @Json(name = "finalDatetime")
    val finalDatetime: String,
    @Json(name = "form")
    val form: Form,
    @Json(name = "formId")
    val formId: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "imageUrl")
    val imageUrl: String,
    @Json(name = "location")
    val location: Location,
    @Json(name = "prize")
    val prize: Int,
    @Json(name = "rules")
    val rules: String,
    @Json(name = "startDatetime")
    val startDatetime: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "time")
    val time: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "type")
    val type: String
)