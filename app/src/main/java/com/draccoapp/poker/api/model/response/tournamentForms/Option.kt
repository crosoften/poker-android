package com.draccoapp.poker.api.model.response.tournamentForms


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Option(
    @Json(name = "option")
    val option: String
)