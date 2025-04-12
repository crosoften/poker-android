package com.draccoapp.poker.api.model.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddUpdadeTournament(
    @Json(name = "subscriptionId")
    val subscriptionId: String,
    @Json(name = "title")
    val title: String? = null,
    @Json(name = "message")
    val message: String? = null,
    @Json(name = "proofUrl")
    val proofUrl: String? = null
)