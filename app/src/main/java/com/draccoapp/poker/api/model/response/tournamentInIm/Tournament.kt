package com.draccoapp.poker.api.model.response.tournamentInIm


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tournament(
    @Json(name = "id")
    val id: String?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "rules")
    val rules: String?,
    @Json(name = "prize")
    val prize: Int?,
    @Json(name = "startDatetime")
    val startDatetime: String?,
    @Json(name = "finalDatetime")
    val finalDatetime: String?,
    @Json(name = "time")
    val time: String?,
    @Json(name = "eventUrl")
    val eventUrl: String?,
    @Json(name = "imageUrl")
    val imageUrl: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "createdAt")
    val createdAt: String?,
    @Json(name = "updatedAt")
    val updatedAt: String?,
    @Json(name = "formId")
    val formId: String?,
    @Json(name = "location")
    val location: LocationX?
)