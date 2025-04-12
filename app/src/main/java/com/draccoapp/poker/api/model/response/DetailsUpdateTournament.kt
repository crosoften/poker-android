package com.draccoapp.poker.api.model.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DetailsUpdateTournament(
    @Json(name = "id")
    val id: String?,
    @Json(name = "subscriptionId")
    val subscriptionId: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "message")
    val message: String?,
    @Json(name = "proofUrl")
    val proofUrl: String?,
    @Json(name = "date")
    val date: String?,
    @Json(name = "createdAt")
    val createdAt: String?,
    @Json(name = "updatedAt")
    val updatedAt: String?
)